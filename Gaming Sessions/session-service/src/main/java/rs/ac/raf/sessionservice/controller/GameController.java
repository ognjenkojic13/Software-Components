package rs.ac.raf.sessionservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.raf.sessionservice.dto.GameRequest;
import rs.ac.raf.sessionservice.dto.GameResponse;
import rs.ac.raf.sessionservice.service.GameService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping
    public ResponseEntity<List<GameResponse>> listAll() {
        return ResponseEntity.ok(gameService.listAll());
    }

    @PostMapping
    public ResponseEntity<GameResponse> create(@Valid @RequestBody GameRequest request) {
        return ResponseEntity.ok(gameService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameResponse> update(@PathVariable Long id, @Valid @RequestBody GameRequest request) {
        return ResponseEntity.ok(gameService.update(id, request));
    }
}
