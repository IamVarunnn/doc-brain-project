package com.docbrain.docbrain_backend.dto;

import com.docbrain.docbrain_backend.entity.Role;

public record RegisterUserRequest(

        String username,
        String email,
        String password
) {

}
