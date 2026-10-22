package com.docbrain.docbrain_backend.controller;


import com.docbrain.docbrain_backend.dto.ApiResponse;
import com.docbrain.docbrain_backend.dto.DocumentMetadataDto;
import com.docbrain.docbrain_backend.dto.DocumentResponseDto;
import com.docbrain.docbrain_backend.entity.DocumentMetadata;
import com.docbrain.docbrain_backend.service.DocumentMetadataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("/api/v1/documents")
@Tag(
        name = "Document Manager",
        description = "Endpoints for uploading, listing and mapping documents."
)
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentMetadataService documentService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload and index a document(PDF, DOCS, TEXT and so on)",
            description = "This api is used to upload and index"

    )
    public ResponseEntity<ApiResponse<DocumentResponseDto>> uploadDocument(
            @RequestParam("file")MultipartFile file
            ){

        DocumentResponseDto documentResponseDto = this.documentService.uploadAndProcess(file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<DocumentResponseDto>builder()
                        .success(true)
                        .data(documentResponseDto)
                        .timeStamp(LocalDateTime.now())
                        .message("Documents uploaded and indexed successfully")
                        .build());
    }

    // API to upload multiple documents

    @PostMapping(value = "/upload-multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload and index multiple documents simultaneously",
            description = "This api is used to upload and index multiple documents"
    )
    public ResponseEntity<ApiResponse<List<DocumentResponseDto>>> uploadMultiple(
            @RequestParam("files")
            List<MultipartFile> files
    ){
        List<DocumentResponseDto> responseDtos = documentService.uploadMultipleDocuments(files);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<List<DocumentResponseDto>>
                                builder()
                                .message("Document upload successfully")
                                .timeStamp(LocalDateTime.now())
                                .data(responseDtos)
                                .build()
                );
    }

    // list all uploaded documents

    @GetMapping
    @Operation(summary = "List all uploaded documents and their indexing status")
    public ResponseEntity<ApiResponse<List<DocumentMetadataDto>>> getAllDocuments(){
        List<DocumentMetadataDto> documents =  documentService.getAllDocuments();

        return ResponseEntity.ok(
                ApiResponse.<List<DocumentMetadataDto>>
                                builder()
                        .message("All documents are here")
                        .timeStamp(LocalDateTime.now())
                        .data(documents)
                        .build()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get metadata of specific document by ID")
    public ResponseEntity<ApiResponse<DocumentMetadataDto>> getDocumentById(@PathVariable UUID id){
        DocumentMetadataDto document = documentService.getDocumentById(id);
        return ResponseEntity.ok(ApiResponse.<DocumentMetadataDto>
                        builder()
                .message("Single document is here")
                .timeStamp(LocalDateTime.now())
                .data(document)
                .build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Get metadata of specific document by ID")
    public ResponseEntity<ApiResponse<Void>> deleteDocument(@PathVariable UUID id){

        documentService.deleteDocument(id);
        return ResponseEntity.ok(ApiResponse.<Void>
                        builder()
                        .message("Document deleted successfully")
                        .success(true)
                        .timeStamp(LocalDateTime.now())
                        .data(null)
                        .build());
    }
}
