package com.streamroom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record GameDTO(
        Long id,

        @NotBlank(message = "Title is required")
        @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
        String title,

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,

        String coverImage,

        @Size(max = 100, message = "Genre must not exceed 100 characters")
        String genre,

        @Size(max = 255, message = "Developer must not exceed 255 characters")
        String developer,

        CategoryDTO category,
        Boolean isFeatured,
        String createdAt
) {
}
