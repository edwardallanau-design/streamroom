package com.streamroom.repository;

import com.streamroom.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    Optional<Content> findBySlug(String slug);
    List<Content> findByIsPublishedTrue();
    List<Content> findByIsFeaturedTrueAndIsPublishedTrue();
    List<Content> findByAuthorIdOrderByCreatedAtDesc(Long authorId);
    List<Content> findByCategoryIdAndIsPublishedTrue(Long categoryId);
}
