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
public class AdminContentController {

    private final IContentService contentService;

    public AdminContentController(IContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping("/admin/content")
    public ResponseEntity<List<ContentDTO>> getAllContent() {
        return ResponseEntity.ok(contentService.getAllContent());
    }

    @PostMapping("/admin/content")
    public ResponseEntity<ContentDTO> createContent(@Valid @RequestBody CreateContentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contentService.createContentAdmin(request));
    }

    @PutMapping("/admin/content/{id}")
    public ResponseEntity<ContentDTO> updateContent(
            @PathVariable Long id,
            @Valid @RequestBody CreateContentRequest request) {
        return ResponseEntity.ok(contentService.updateContent(id, request));
    }

    @DeleteMapping("/admin/content/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        contentService.deleteContent(id);
        return ResponseEntity.noContent().build();
    }
}
