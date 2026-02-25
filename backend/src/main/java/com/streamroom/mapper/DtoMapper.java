package com.streamroom.mapper;

import com.streamroom.dto.CategoryDTO;
import com.streamroom.dto.ContentDTO;
import com.streamroom.dto.GameDTO;
import com.streamroom.dto.UserDTO;
import com.streamroom.entity.Category;
import com.streamroom.entity.Content;
import com.streamroom.entity.Game;
import com.streamroom.entity.User;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    public UserDTO toUserDTO(User user) {
        if (user == null) return null;
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .bio(user.getBio())
                .profileImage(user.getProfileImage())
                .bannerImage(user.getBannerImage())
                .twitchUsername(user.getTwitchUsername())
                .build();
    }

    public CategoryDTO toCategoryDTO(Category category) {
        if (category == null) return null;
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .icon(category.getIcon())
                .build();
    }

    public ContentDTO toContentDTO(Content content) {
        if (content == null) return null;
        return ContentDTO.builder()
                .id(content.getId())
                .title(content.getTitle())
                .description(content.getDescription())
                .content(content.getContent())
                .slug(content.getSlug())
                .featuredImage(content.getFeaturedImage())
                .author(toUserDTO(content.getAuthor()))
                .category(toCategoryDTO(content.getCategory()))
                .isPublished(content.getIsPublished())
                .isFeatured(content.getIsFeatured())
                .createdAt(content.getCreatedAt())
                .updatedAt(content.getUpdatedAt())
                .publishedAt(content.getPublishedAt())
                .build();
    }

    public GameDTO toGameDTO(Game game) {
        if (game == null) return null;
        return GameDTO.builder()
                .id(game.getId())
                .title(game.getTitle())
                .description(game.getDescription())
                .coverImage(game.getCoverImage())
                .genre(game.getGenre())
                .developer(game.getDeveloper())
                .category(toCategoryDTO(game.getCategory()))
                .isFeatured(game.getIsFeatured())
                .createdAt(game.getCreatedAt())
                .build();
    }
}
