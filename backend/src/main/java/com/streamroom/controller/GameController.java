package com.streamroom.controller;

import com.streamroom.dto.GameDTO;
import com.streamroom.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class GameController {
    private final GameService gameService;

    @PostMapping
    public ResponseEntity<GameDTO> createGame(@RequestBody GameDTO gameDTO) {
        GameDTO created = gameService.createGame(gameDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameDTO> updateGame(
            @PathVariable Long id,
            @RequestBody GameDTO gameDTO) {
        GameDTO updated = gameService.updateGame(id, gameDTO);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameDTO> getGame(@PathVariable Long id) {
        GameDTO gameDTO = gameService.getGameById(id);
        return ResponseEntity.ok(gameDTO);
    }

    @GetMapping
    public ResponseEntity<List<GameDTO>> getAllGames() {
        List<GameDTO> games = gameService.getAllGames();
        return ResponseEntity.ok(games);
    }

    @GetMapping("/featured")
    public ResponseEntity<List<GameDTO>> getFeaturedGames() {
        List<GameDTO> games = gameService.getFeaturedGames();
        return ResponseEntity.ok(games);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }
}
