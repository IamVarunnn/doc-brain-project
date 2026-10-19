package com.docbrain.docbrain_backend.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/documents")
@Tag(
        name = "Document Manager",
        description = "Endpoints for uploading, listing and mapping documents."
)
public class DocumentController {

    @PostMapping
    @Operation(
            summary = "Upload and index a document(PDF, DOCS, TEXT and so on)",
            description = "This api is used to upload and index"

    )
    public ResponseEntity<String> uploadDocument(){
        return ResponseEntity.ok("Uploaded");
    }
}
