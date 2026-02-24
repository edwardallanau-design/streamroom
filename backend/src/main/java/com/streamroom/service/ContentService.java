package com.streamroom.service;

import com.streamroom.dto.ContentDTO;
import com.streamroom.dto.CreateContentRequest;
import com.streamroom.entity.Content;
import com.streamroom.entity.Category;
import com.streamroom.entity.User;
import com.streamroom.repository.ContentRepository;
import com.streamroom.repository.CategoryRepository;
import com.streamroom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public ContentDTO createContent(CreateContentRequest request, Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Content content = Content.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .content(request.getContent())
                .slug(generateSlug(request.getTitle()))
                .featuredImage(request.getFeaturedImage())
                .author(author)
                .isPublished(request.getIsPublished() != null ? request.getIsPublished() : false)
                .isFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false)
                .build();

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            content.setCategory(category);
        }

        content = contentRepository.save(content);
        return mapToDTO(content);
    }

    public ContentDTO updateContent(Long id, CreateContentRequest request) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Content not found"));

        content.setTitle(request.getTitle());
        content.setDescription(request.getDescription());
        content.setContent(request.getContent());
        content.setFeaturedImage(request.getFeaturedImage());
        content.setIsPublished(request.getIsPublished() != null ? request.getIsPublished() : content.getIsPublished());
        content.setIsFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : content.getIsFeatured());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            content.setCategory(category);
        }

        content = contentRepository.save(content);
        return mapToDTO(content);
    }

    public ContentDTO getContentById(Long id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Content not found"));
        return mapToDTO(content);
    }

    public ContentDTO getContentBySlug(String slug) {
        Content content = contentRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException("Content not found"));
        return mapToDTO(content);
    }

    public List<ContentDTO> getAllPublishedContent() {
        return contentRepository.findByIsPublishedTrue()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ContentDTO> getFeaturedContent() {
        return contentRepository.findByIsFeaturedTrueAndIsPublishedTrue()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ContentDTO> getContentByAuthor(Long authorId) {
        return contentRepository.findByAuthorIdOrderByCreatedAtDesc(authorId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void deleteContent(Long id) {
        contentRepository.deleteById(id);
    }

    private ContentDTO mapToDTO(Content content) {
        return ContentDTO.builder()
                .id(content.getId())
                .title(content.getTitle())
                .description(content.getDescription())
                .content(content.getContent())
                .slug(content.getSlug())
                .featuredImage(content.getFeaturedImage())
                .isPublished(content.getIsPublished())
                .isFeatured(content.getIsFeatured())
                .createdAt(content.getCreatedAt())
                .updatedAt(content.getUpdatedAt())
                .publishedAt(content.getPublishedAt())
                .build();
    }

    private String generateSlug(String title) {
        return title.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");
    }
}
