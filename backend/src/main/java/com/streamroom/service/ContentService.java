package com.streamroom.service;

import com.streamroom.dto.ContentDTO;
import com.streamroom.dto.CreateContentRequest;
import com.streamroom.entity.Category;
import com.streamroom.entity.Content;
import com.streamroom.entity.User;
import com.streamroom.exception.ResourceNotFoundException;
import com.streamroom.mapper.DtoMapper;
import com.streamroom.repository.CategoryRepository;
import com.streamroom.repository.ContentRepository;
import com.streamroom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ContentService implements IContentService {

    private final ContentRepository contentRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final DtoMapper mapper;
    private final SlugGeneratorService slugGenerator;

    @Override
    @Transactional
    public ContentDTO createContent(CreateContentRequest request, Long authorId) {
        log.info("Creating content '{}' for author id={}", request.getTitle(), authorId);

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("User", authorId));

        Content content = Content.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .content(request.getContent())
                .slug(slugGenerator.generate(request.getTitle()))
                .featuredImage(request.getFeaturedImage())
                .author(author)
                .isPublished(request.getIsPublished() != null ? request.getIsPublished() : false)
                .isFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false)
                .build();

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));
            content.setCategory(category);
        }

        return mapper.toContentDTO(contentRepository.save(content));
    }

    @Override
    @Transactional
    public ContentDTO updateContent(Long id, CreateContentRequest request) {
        log.info("Updating content id={}", id);

        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Content", id));

        content.setTitle(request.getTitle());
        content.setDescription(request.getDescription());
        content.setContent(request.getContent());
        content.setFeaturedImage(request.getFeaturedImage());
        content.setIsPublished(request.getIsPublished() != null ? request.getIsPublished() : content.getIsPublished());
        content.setIsFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : content.getIsFeatured());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));
            content.setCategory(category);
        }

        return mapper.toContentDTO(contentRepository.save(content));
    }

    @Override
    public ContentDTO getContentById(Long id) {
        return mapper.toContentDTO(contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Content", id)));
    }

    @Override
    public ContentDTO getContentBySlug(String slug) {
        return mapper.toContentDTO(contentRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Content", slug)));
    }

    @Override
    public List<ContentDTO> getAllPublishedContent() {
        return contentRepository.findByIsPublishedTrue()
                .stream().map(mapper::toContentDTO).toList();
    }

    @Override
    public List<ContentDTO> getFeaturedContent() {
        return contentRepository.findByIsFeaturedTrueAndIsPublishedTrue()
                .stream().map(mapper::toContentDTO).toList();
    }

    @Override
    public List<ContentDTO> getContentByAuthor(Long authorId) {
        return contentRepository.findByAuthorIdOrderByCreatedAtDesc(authorId)
                .stream().map(mapper::toContentDTO).toList();
    }

    @Override
    @Transactional
    public void deleteContent(Long id) {
        log.info("Deleting content id={}", id);
        if (!contentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Content", id);
        }
        contentRepository.deleteById(id);
    }
}
