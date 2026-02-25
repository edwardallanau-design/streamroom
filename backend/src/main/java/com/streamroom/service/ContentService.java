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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ContentService implements IContentService {

    private static final Logger log = LoggerFactory.getLogger(ContentService.class);

    private final ContentRepository contentRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final DtoMapper mapper;
    private final SlugGeneratorService slugGenerator;

    public ContentService(ContentRepository contentRepository, UserRepository userRepository,
                          CategoryRepository categoryRepository, DtoMapper mapper,
                          SlugGeneratorService slugGenerator) {
        this.contentRepository = contentRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
        this.slugGenerator = slugGenerator;
    }

    @Override
    @Transactional
    public ContentDTO createContent(CreateContentRequest request, Long authorId) {
        log.info("Creating content '{}' for author id={}", request.title(), authorId);

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("User", authorId));

        var content = new Content();
        content.setTitle(request.title());
        content.setDescription(request.description());
        content.setContent(request.content());
        content.setSlug(slugGenerator.generate(request.title()));
        content.setFeaturedImage(request.featuredImage());
        content.setAuthor(author);

        if (request.categoryId() != null) {
            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", request.categoryId()));
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

        content.setTitle(request.title());
        content.setDescription(request.description());
        content.setContent(request.content());
        content.setFeaturedImage(request.featuredImage());
        content.setIsPublished(request.isPublished() != null ? request.isPublished() : content.getIsPublished());
        content.setIsFeatured(request.isFeatured() != null ? request.isFeatured() : content.getIsFeatured());

        if (request.categoryId() != null) {
            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", request.categoryId()));
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
