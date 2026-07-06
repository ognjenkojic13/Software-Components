package rs.ac.raf.sessionservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.raf.sessionservice.dto.GameRequest;
import rs.ac.raf.sessionservice.dto.GameResponse;
import rs.ac.raf.sessionservice.entity.Game;
import rs.ac.raf.sessionservice.exception.ConflictException;
import rs.ac.raf.sessionservice.exception.NotFoundException;
import rs.ac.raf.sessionservice.repository.GameRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<GameResponse> listAll() {
        return gameRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public GameResponse create(GameRequest request) {
        if (gameRepository.existsByNameIgnoreCase(request.getName())) {
            throw new ConflictException("Igra sa ovim imenom vec postoji");
        }
        Game game = new Game();
        applyRequest(game, request);
        return toResponse(gameRepository.save(game));
    }

    @Transactional
    public GameResponse update(Long id, GameRequest request) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Igra ne postoji: " + id));
        applyRequest(game, request);
        return toResponse(gameRepository.save(game));
    }

    Game getGameOrThrow(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Igra ne postoji: " + id));
    }

    private void applyRequest(Game game, GameRequest request) {
        game.setName(request.getName());
        game.setDescription(request.getDescription());
        game.setGenre(request.getGenre());
    }

    private GameResponse toResponse(Game game) {
        return new GameResponse(game.getId(), game.getName(), game.getDescription(), game.getGenre());
    }
}
