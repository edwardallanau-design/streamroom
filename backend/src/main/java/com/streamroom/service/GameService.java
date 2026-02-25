package com.streamroom.service;

import com.streamroom.dto.GameDTO;
import com.streamroom.entity.Game;
import com.streamroom.exception.ResourceNotFoundException;
import com.streamroom.mapper.DtoMapper;
import com.streamroom.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class GameService implements IGameService {

    private final GameRepository gameRepository;
    private final DtoMapper mapper;

    @Override
    @Transactional
    public GameDTO createGame(GameDTO gameDTO) {
        log.info("Creating game '{}'", gameDTO.getTitle());

        Game game = Game.builder()
                .title(gameDTO.getTitle())
                .description(gameDTO.getDescription())
                .coverImage(gameDTO.getCoverImage())
                .genre(gameDTO.getGenre())
                .developer(gameDTO.getDeveloper())
                .isFeatured(false)
                .build();

        return mapper.toGameDTO(gameRepository.save(game));
    }

    @Override
    @Transactional
    public GameDTO updateGame(Long id, GameDTO gameDTO) {
        log.info("Updating game id={}", id);

        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game", id));

        game.setTitle(gameDTO.getTitle());
        game.setDescription(gameDTO.getDescription());
        game.setCoverImage(gameDTO.getCoverImage());
        game.setGenre(gameDTO.getGenre());
        game.setDeveloper(gameDTO.getDeveloper());
        game.setIsFeatured(gameDTO.getIsFeatured());

        return mapper.toGameDTO(gameRepository.save(game));
    }

    @Override
    public GameDTO getGameById(Long id) {
        return mapper.toGameDTO(gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game", id)));
    }

    @Override
    public List<GameDTO> getAllGames() {
        return gameRepository.findAll().stream().map(mapper::toGameDTO).toList();
    }

    @Override
    public List<GameDTO> getFeaturedGames() {
        return gameRepository.findByIsFeaturedTrue().stream().map(mapper::toGameDTO).toList();
    }

    @Override
    @Transactional
    public void deleteGame(Long id) {
        log.info("Deleting game id={}", id);
        if (!gameRepository.existsById(id)) {
            throw new ResourceNotFoundException("Game", id);
        }
        gameRepository.deleteById(id);
    }
}
