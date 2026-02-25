package com.streamroom.dto;


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
        String createdAt,
        String updatedAt,
        String publishedAt
) {
}
