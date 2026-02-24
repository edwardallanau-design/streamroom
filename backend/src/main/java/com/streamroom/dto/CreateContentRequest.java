package com.streamroom.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateContentRequest {
    private String title;
    private String description;
    private String content;
    private String slug;
    private String featuredImage;
    private Long categoryId;
    private Boolean isPublished;
    private Boolean isFeatured;
}
