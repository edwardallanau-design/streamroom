package com.streamroom.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentDTO {
    private Long id;
    private String title;
    private String description;
    private String content;
    private String slug;
    private String featuredImage;
    private UserDTO author;
    private CategoryDTO category;
    private Boolean isPublished;
    private Boolean isFeatured;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;
}
