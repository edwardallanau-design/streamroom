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
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getDisplayName(),
                user.getBio(),
                user.getProfileImage(),
                user.getBannerImage(),
                user.getTwitchUsername()
        );
    }

    public CategoryDTO toCategoryDTO(Category category) {
        if (category == null) return null;
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getIcon()
        );
    }

    public ContentDTO toContentDTO(Content content) {
        if (content == null) return null;
        return new ContentDTO(
                content.getId(),
                content.getTitle(),
                content.getDescription(),
                content.getContent(),
                content.getSlug(),
                content.getFeaturedImage(),
                toUserDTO(content.getAuthor()),
                toCategoryDTO(content.getCategory()),
                content.getIsPublished(),
                content.getIsFeatured(),
                content.getCreatedAt(),
                content.getUpdatedAt(),
                content.getPublishedAt()
        );
    }

    public GameDTO toGameDTO(Game game) {
        if (game == null) return null;
        return new GameDTO(
                game.getId(),
                game.getTitle(),
                game.getDescription(),
                game.getCoverImage(),
                game.getGenre(),
                game.getDeveloper(),
                toCategoryDTO(game.getCategory()),
                game.getIsFeatured(),
                game.getCreatedAt()
        );
    }
}
