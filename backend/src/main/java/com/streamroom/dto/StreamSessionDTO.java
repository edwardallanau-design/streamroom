package com.streamroom.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StreamSessionDTO {
    private Long id;
    private UserDTO user;
    private String title;
    private String description;
    private GameDTO game;
    private Boolean isLive;
    private String twitchStreamUrl;
    private String thumbnail;
    private Integer viewerCount;
    private LocalDateTime createdAt;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
}
