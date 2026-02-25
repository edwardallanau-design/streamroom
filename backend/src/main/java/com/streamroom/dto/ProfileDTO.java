package com.streamroom.dto;

public record ProfileDTO(
        String displayName,
        String tagline,
        String bio,
        String profileImage,
        String twitchUsername,
        String schedule,
        String socials,
        Integer followerCount,
        Integer streamCount,
        Integer hoursStreamed
) {}
