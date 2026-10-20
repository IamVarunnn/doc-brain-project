package com.docbrain.docbrain_backend.controller;


import com.docbrain.docbrain_backend.dto.ApiResponse;
import com.docbrain.docbrain_backend.dto.DocumentResponseDto;
import com.docbrain.docbrain_backend.service.DocumentMetadataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

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
}
