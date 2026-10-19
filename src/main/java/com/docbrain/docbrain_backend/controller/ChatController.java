package com.docbrain.docbrain_backend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")

@Tag(
        name = "Chat Manager",
        description = "Endpoints for all chats related apis"
)

public class ChatController {

    @PostMapping
    public ResponseEntity<String> chat(){
        return ResponseEntity.ok("This is just testing configuration");
    }
}
