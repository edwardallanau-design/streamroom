package com.streamroom.controller;

import com.streamroom.dto.ContentDTO;
import com.streamroom.dto.CreateContentRequest;
import com.streamroom.enums.Role;
import com.streamroom.exception.ForbiddenException;
import com.streamroom.service.IContentService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<ContentDTO> createContent(
            @Valid @RequestBody CreateContentRequest body,
            HttpServletRequest request) {
        String roleStr = (String) request.getAttribute("role");
        Long actorId = (Long) request.getAttribute("userId");
        Role role = Role.valueOf(roleStr);

        // Content creators cannot publish — strip publish flag
        CreateContentRequest effective = body;
        if (role == Role.CONTENT_CREATOR && Boolean.TRUE.equals(body.isPublished())) {
            effective = new CreateContentRequest(
                    body.title(), body.description(), body.content(),
                    body.slug(), body.featuredImage(), body.categoryId(),
                    false, body.isFeatured()
            );
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contentService.createContentAdmin(effective, actorId));
    }

    @PutMapping("/admin/content/{id}")
    public ResponseEntity<ContentDTO> updateContent(
            @PathVariable Long id,
            @Valid @RequestBody CreateContentRequest body,
            HttpServletRequest request) {
        String roleStr = (String) request.getAttribute("role");
        Long actorId = (Long) request.getAttribute("userId");
        Role role = Role.valueOf(roleStr);

        if (role == Role.CONTENT_CREATOR) {
            ContentDTO existing = contentService.getContentById(id);
            if (existing.author() == null || !actorId.equals(existing.author().id())) {
                throw new ForbiddenException("You can only edit your own posts");
            }
            // Strip publish flag — content creators cannot publish
            body = new CreateContentRequest(
                    body.title(), body.description(), body.content(),
                    body.slug(), body.featuredImage(), body.categoryId(),
                    existing.isPublished(), body.isFeatured()
            );
        }

        return ResponseEntity.ok(contentService.updateContent(id, body));
    }

    @PatchMapping("/admin/content/{id}/publish")
    public ResponseEntity<ContentDTO> publishContent(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, Boolean> body,
            HttpServletRequest request) {
        String roleStr = (String) request.getAttribute("role");
        Role role = Role.valueOf(roleStr);

        if (role == Role.CONTENT_CREATOR) {
            throw new ForbiddenException("Only admins and moderators can publish posts");
        }

        Boolean isPublished = body.get("isPublished");
        return ResponseEntity.ok(contentService.publishContent(id, isPublished));
    }

    @DeleteMapping("/admin/content/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id, HttpServletRequest request) {
        String roleStr = (String) request.getAttribute("role");
        Long actorId = (Long) request.getAttribute("userId");
        Role role = Role.valueOf(roleStr);

        if (role == Role.CONTENT_CREATOR) {
            ContentDTO existing = contentService.getContentById(id);
            if (existing.author() == null || !actorId.equals(existing.author().id())) {
                throw new ForbiddenException("You can only delete your own posts");
            }
        }

        contentService.deleteContent(id);
        return ResponseEntity.noContent().build();
    }
}
