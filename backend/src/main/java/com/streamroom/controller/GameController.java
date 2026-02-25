package com.streamroom.controller;

import com.streamroom.dto.GameDTO;
import com.streamroom.service.IGameService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    private final IGameService gameService;

    public GameController(IGameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<GameDTO> createGame(@Valid @RequestBody GameDTO gameDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gameService.createGame(gameDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameDTO> updateGame(
            @PathVariable Long id,
            @Valid @RequestBody GameDTO gameDTO) {
        return ResponseEntity.ok(gameService.updateGame(id, gameDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameDTO> getGame(@PathVariable Long id) {
        return ResponseEntity.ok(gameService.getGameById(id));
    }

    @GetMapping
    public ResponseEntity<List<GameDTO>> getAllGames() {
        return ResponseEntity.ok(gameService.getAllGames());
    }

    @GetMapping("/featured")
    public ResponseEntity<List<GameDTO>> getFeaturedGames() {
        return ResponseEntity.ok(gameService.getFeaturedGames());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }
}
