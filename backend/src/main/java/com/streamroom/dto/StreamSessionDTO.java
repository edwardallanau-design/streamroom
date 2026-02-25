package com.streamroom.dto;

import java.time.LocalDateTime;

public record StreamSessionDTO(
        Long id,
        UserDTO user,
        String title,
        String description,
        GameDTO game,
        Boolean isLive,
        String twitchStreamUrl,
        String thumbnail,
        Integer viewerCount,
        LocalDateTime createdAt,
        LocalDateTime startedAt,
        LocalDateTime endedAt
) {
}
