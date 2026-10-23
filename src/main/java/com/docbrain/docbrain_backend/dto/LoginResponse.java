package com.docbrain.docbrain_backend.dto;

public record LoginResponse(
        String accessToken,
        UserDto user
) {
}
