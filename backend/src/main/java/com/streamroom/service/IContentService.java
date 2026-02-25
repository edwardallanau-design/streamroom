package com.streamroom.service;

import com.streamroom.dto.ContentDTO;
import com.streamroom.dto.CreateContentRequest;

import java.util.List;

public interface IContentService {
    ContentDTO createContent(CreateContentRequest request, Long authorId);

    ContentDTO updateContent(Long id, CreateContentRequest request);

    ContentDTO getContentById(Long id);

    ContentDTO getContentBySlug(String slug);

    List<ContentDTO> getAllPublishedContent();

    List<ContentDTO> getFeaturedContent();

    List<ContentDTO> getContentByAuthor(Long authorId);

    void deleteContent(Long id);

    List<ContentDTO> getAllContent();

    ContentDTO createContentAdmin(CreateContentRequest request, Long authorId);

    ContentDTO publishContent(Long id, Boolean isPublished);
}
