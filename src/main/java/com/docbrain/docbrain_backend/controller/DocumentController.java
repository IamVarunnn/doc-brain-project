package com.docbrain.docbrain_backend.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/documents")
public class DocumentController {

    @PostMapping
    public ResponseEntity<String> uploadDocument(){
        return ResponseEntity.ok("Uploaded");
    }
}
