package com.docbrain.docbrain_backend.dto;

public record LoginRequest(
        String username,
        String password
) {
}
