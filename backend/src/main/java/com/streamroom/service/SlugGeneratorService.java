package com.streamroom.service;

import org.springframework.stereotype.Service;

@Service
public class SlugGeneratorService {

    public String generate(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be blank");
        }
        return title.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");
    }
}
