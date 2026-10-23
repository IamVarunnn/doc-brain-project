package com.docbrain.docbrain_backend.dto;

import com.docbrain.docbrain_backend.entity.Role;

public record UserDto(
        Long id,
        String username,
        String email,
        Role role
) {
}
