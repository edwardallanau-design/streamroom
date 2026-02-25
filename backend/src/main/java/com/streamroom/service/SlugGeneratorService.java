package com.streamroom.service;

import com.streamroom.repository.ContentRepository;
import org.springframework.stereotype.Service;

@Service
public class SlugGeneratorService {

    private final ContentRepository contentRepository;

    public SlugGeneratorService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    public String generate(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be blank");
        }
        String base = title.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");

        if (contentRepository.findBySlug(base).isEmpty()) {
            return base;
        }

        int suffix = 2;
        String candidate;
        do {
            candidate = base + "-" + suffix++;
        } while (contentRepository.findBySlug(candidate).isPresent());

        return candidate;
    }
}
