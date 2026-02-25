package com.streamroom.controller;

import com.streamroom.dto.ContentDTO;
import com.streamroom.dto.CreateContentRequest;
import com.streamroom.service.IContentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content")
public class ContentController {

    private final IContentService contentService;

    public ContentController(IContentService contentService) {
        this.contentService = contentService;
    }

    @PostMapping
    public ResponseEntity<ContentDTO> createContent(
            @Valid @RequestBody CreateContentRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contentService.createContent(request, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContentDTO> updateContent(
            @PathVariable Long id,
            @Valid @RequestBody CreateContentRequest request) {
        return ResponseEntity.ok(contentService.updateContent(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentDTO> getContent(@PathVariable Long id) {
        return ResponseEntity.ok(contentService.getContentById(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ContentDTO> getContentBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(contentService.getContentBySlug(slug));
    }

    @GetMapping
    public ResponseEntity<List<ContentDTO>> getAllPublishedContent() {
        return ResponseEntity.ok(contentService.getAllPublishedContent());
    }

    @GetMapping("/featured")
    public ResponseEntity<List<ContentDTO>> getFeaturedContent() {
        return ResponseEntity.ok(contentService.getFeaturedContent());
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<ContentDTO>> getContentByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(contentService.getContentByAuthor(authorId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        contentService.deleteContent(id);
        return ResponseEntity.noContent().build();
    }
}
