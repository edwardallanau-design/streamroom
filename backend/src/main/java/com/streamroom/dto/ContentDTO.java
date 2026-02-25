package com.streamroom.dto;

import java.time.LocalDateTime;

public record ContentDTO(
        Long id,
        String title,
        String description,
        String content,
        String slug,
        String featuredImage,
        UserDTO author,
        CategoryDTO category,
        Boolean isPublished,
        Boolean isFeatured,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime publishedAt
) {
}
