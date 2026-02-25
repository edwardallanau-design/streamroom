package com.streamroom.service;

import com.streamroom.dto.GameDTO;

import java.util.List;

public interface IGameService {
    GameDTO createGame(GameDTO gameDTO);
    GameDTO updateGame(Long id, GameDTO gameDTO);
    GameDTO getGameById(Long id);
    List<GameDTO> getAllGames();
    List<GameDTO> getFeaturedGames();
    void deleteGame(Long id);
}
