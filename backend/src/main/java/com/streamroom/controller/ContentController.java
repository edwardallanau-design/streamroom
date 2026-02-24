package com.streamroom.controller;

import com.streamroom.dto.ContentDTO;
import com.streamroom.dto.CreateContentRequest;
import com.streamroom.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class ContentController {
    private final ContentService contentService;

    @PostMapping
    public ResponseEntity<ContentDTO> createContent(
            @RequestBody CreateContentRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        ContentDTO contentDTO = contentService.createContent(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(contentDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContentDTO> updateContent(
            @PathVariable Long id,
            @RequestBody CreateContentRequest request) {
        ContentDTO contentDTO = contentService.updateContent(id, request);
        return ResponseEntity.ok(contentDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentDTO> getContent(@PathVariable Long id) {
        ContentDTO contentDTO = contentService.getContentById(id);
        return ResponseEntity.ok(contentDTO);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ContentDTO> getContentBySlug(@PathVariable String slug) {
        ContentDTO contentDTO = contentService.getContentBySlug(slug);
        return ResponseEntity.ok(contentDTO);
    }

    @GetMapping
    public ResponseEntity<List<ContentDTO>> getAllPublishedContent() {
        List<ContentDTO> content = contentService.getAllPublishedContent();
        return ResponseEntity.ok(content);
    }

    @GetMapping("/featured")
    public ResponseEntity<List<ContentDTO>> getFeaturedContent() {
        List<ContentDTO> content = contentService.getFeaturedContent();
        return ResponseEntity.ok(content);
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<ContentDTO>> getContentByAuthor(@PathVariable Long authorId) {
        List<ContentDTO> content = contentService.getContentByAuthor(authorId);
        return ResponseEntity.ok(content);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        contentService.deleteContent(id);
        return ResponseEntity.noContent().build();
    }
}
