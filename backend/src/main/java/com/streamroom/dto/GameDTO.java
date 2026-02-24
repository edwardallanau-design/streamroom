package com.streamroom.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameDTO {
    private Long id;
    private String title;
    private String description;
    private String coverImage;
    private String genre;
    private String developer;
    private CategoryDTO category;
    private Boolean isFeatured;
    private LocalDateTime createdAt;
}
