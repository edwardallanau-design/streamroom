package com.streamroom.dto;

import jakarta.validation.constraints.Size;

public record ProfileUpdateRequest(
        @Size(max = 100) String displayName,
        @Size(max = 200) String tagline,
        @Size(max = 2000) String bio,
        @Size(max = 500) String profileImage,
        @Size(max = 100) String twitchUsername,
        String schedule,
        String socials,
        Integer followerCount,
        Integer streamCount,
        Integer hoursStreamed
) {}
