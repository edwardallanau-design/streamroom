package com.streamroom.dto;

public record AdminUserDTO(
        Long id,
        String username,
        String displayName,
        String email,
        String role
) {}
