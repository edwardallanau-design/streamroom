package com.streamroom.dto;

public record ProfileDTO(
        String displayName,
        String tagline,
        String bio,
        String profileImage,
        String twitchUsername,
        String twitchUrl,
        String discordUrl,
        String twitterUrl,
        String schedule,
        Integer followerCount,
        Integer streamCount,
        Integer hoursStreamed
) {}
