package com.streamroom.service;

import com.streamroom.dto.GameDTO;
import com.streamroom.entity.Game;
import com.streamroom.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;

    public GameDTO createGame(GameDTO gameDTO) {
        Game game = Game.builder()
                .title(gameDTO.getTitle())
                .description(gameDTO.getDescription())
                .coverImage(gameDTO.getCoverImage())
                .genre(gameDTO.getGenre())
                .developer(gameDTO.getDeveloper())
                .isFeatured(false)
                .build();

        game = gameRepository.save(game);
        return mapToDTO(game);
    }

    public GameDTO updateGame(Long id, GameDTO gameDTO) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game not found"));

        game.setTitle(gameDTO.getTitle());
        game.setDescription(gameDTO.getDescription());
        game.setCoverImage(gameDTO.getCoverImage());
        game.setGenre(gameDTO.getGenre());
        game.setDeveloper(gameDTO.getDeveloper());
        game.setIsFeatured(gameDTO.getIsFeatured());

        game = gameRepository.save(game);
        return mapToDTO(game);
    }

    public GameDTO getGameById(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game not found"));
        return mapToDTO(game);
    }

    public List<GameDTO> getAllGames() {
        return gameRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<GameDTO> getFeaturedGames() {
        return gameRepository.findByIsFeaturedTrue()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }

    private GameDTO mapToDTO(Game game) {
        return GameDTO.builder()
                .id(game.getId())
                .title(game.getTitle())
                .description(game.getDescription())
                .coverImage(game.getCoverImage())
                .genre(game.getGenre())
                .developer(game.getDeveloper())
                .isFeatured(game.getIsFeatured())
                .createdAt(game.getCreatedAt())
                .build();
    }
}
