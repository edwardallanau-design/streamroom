package com.streamroom.dto;

public record LoginResponse(
        String token,
        Long userId,
        String username,
        String displayName
) {}
