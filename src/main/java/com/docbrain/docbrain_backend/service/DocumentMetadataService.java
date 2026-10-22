package com.docbrain.docbrain_backend.service;

import com.docbrain.docbrain_backend.dto.DocumentMetadataDto;
import com.docbrain.docbrain_backend.dto.DocumentResponseDto;
import com.docbrain.docbrain_backend.entity.DocumentMetadata;
import com.docbrain.docbrain_backend.entity.DocumentStatus;
import com.docbrain.docbrain_backend.exception.DocumentProcessingException;
import com.docbrain.docbrain_backend.exception.ResourceNotFoundException;
import com.docbrain.docbrain_backend.respositry.DocumentMetadataRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentMetadataService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentMetadataService.class);

    private final DocumentMetadataRepo documentMetadataRepo;
    private final DocumentParserService parserService;
    private final DocumentIngestionService documentIngestionService;
    private final JdbcTemplate jdbcTemplate;
    private final ModelMapper modelMapper;

    public DocumentResponseDto uploadAndProcess(MultipartFile file) {

        String fileName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "document";
        String contentType = file.getContentType() != null ? file.getContentType() : "application/octat-stream";

        // document meta data create
        DocumentMetadata documentMetadata = DocumentMetadata
                .builder()
                .filename(fileName)
                .contentType(contentType)
                .status(DocumentStatus.UPLOADING)
                .fileSize(file.getSize())
                .createdAt(LocalDateTime.now())
                .build();

        List<Document> parsedDocs = null;
        int chunksCreated = 0;

        try {
            // parse the file
            parsedDocs = parserService.parse(file);

            // ingest service
            chunksCreated = documentIngestionService.ingest(documentMetadata, parsedDocs);
        }
        catch (DocumentProcessingException e){
            logger.info("Document metadata due to fail processing");
            documentMetadataRepo.delete(documentMetadata);
            throw e;
        }

        // documentMetadata.setTotalChunks(chunksCreated);

        // save the document metadata
        DocumentMetadata savedDocumentMetadata =  documentMetadataRepo.save(documentMetadata);

        return DocumentResponseDto.builder()
                .id(documentMetadata.getId())
                .fileName(documentMetadata.getFilename())
                .fileSize(documentMetadata.getFileSize())
                .chunksCreated(chunksCreated)
                .status(documentMetadata.getStatus())
                .message("Document successfully processed and indexed")
                .build();
    }

    public List<DocumentResponseDto> uploadMultipleDocuments(List<MultipartFile> files) {

        List<DocumentResponseDto> responseDtos = new ArrayList<>();

        for(MultipartFile file : files){
           DocumentResponseDto result = this.uploadAndProcess(file);
           responseDtos.add(result);
        }
        return responseDtos;
    }

    public List<DocumentMetadataDto> getAllDocuments() {

        List<DocumentMetadata> allDocuments = documentMetadataRepo.findAllByOrderByCreatedAtDesc();

        return allDocuments.stream()
                .map(documentMetadata -> modelMapper.map(documentMetadata, DocumentMetadataDto.class))
                .toList();
    }


    public DocumentMetadataDto getDocumentById(UUID id) {
        DocumentMetadata documentMetadata = documentMetadataRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Document with given id not found"));
        return modelMapper.map(documentMetadata, DocumentMetadataDto.class);

    }

    public void deleteDocument(UUID id) {
        DocumentMetadata documentMetadata = documentMetadataRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Document with given id not found"));

        // delete the vector entries
        try {
            String deleteVectorSql = "DELETE FROM vector_store WHERE metadata->>'documentId' = ?";
            int deletedCount = jdbcTemplate.update(deleteVectorSql, id.toString());
            logger.info("Deleted {} vector chunks for document id {}", deletedCount, id);
        }
        catch (Exception e){
            logger.warn("could not delete vectors from vector store directly {}", e.getMessage());
        }
    }
}
