package com.streamroom.service;

import com.streamroom.dto.GameDTO;
import com.streamroom.entity.Game;
import com.streamroom.exception.ResourceNotFoundException;
import com.streamroom.mapper.DtoMapper;
import com.streamroom.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GameService implements IGameService {

    private static final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;
    private final DtoMapper mapper;

    public GameService(GameRepository gameRepository, DtoMapper mapper) {
        this.gameRepository = gameRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public GameDTO createGame(GameDTO gameDTO) {
        log.info("Creating game '{}'", gameDTO.title());

        var game = new Game();
        game.setTitle(gameDTO.title());
        game.setDescription(gameDTO.description());
        game.setCoverImage(gameDTO.coverImage());
        game.setGenre(gameDTO.genre());
        game.setDeveloper(gameDTO.developer());

        return mapper.toGameDTO(gameRepository.save(game));
    }

    @Override
    @Transactional
    public GameDTO updateGame(Long id, GameDTO gameDTO) {
        log.info("Updating game id={}", id);

        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game", id));

        game.setTitle(gameDTO.title());
        game.setDescription(gameDTO.description());
        game.setCoverImage(gameDTO.coverImage());
        game.setGenre(gameDTO.genre());
        game.setDeveloper(gameDTO.developer());
        game.setIsFeatured(gameDTO.isFeatured());

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
