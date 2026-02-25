package com.streamroom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserAdminRequest(
        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Password is required")
        String password,

        String displayName,

        @NotBlank(message = "Email is required")
        String email,

        @NotNull(message = "Role is required")
        String role
) {}
