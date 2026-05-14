package com.evjf.service;

import com.evjf.dto.GetCardDTO;
import com.evjf.dto.GetGameDTO;
import com.evjf.entity.Game;
import com.evjf.repository.GameRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GameService {

    // ATTRIBUTES

    private final GameRepository gameRepository;

    // CONSTRUCTORS

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    // METHOD

    @Transactional
    public GetGameDTO getGame(Integer id) {
        Game game = gameRepository.findById(id).orElse(null);
        if (game != null) {
            return new GetGameDTO(id, game.getName(), game.getDescription(), game.getNumberRound(),
                    game.getCards().stream().map((card -> new GetCardDTO(card.getId(), card.getQuestion(),
                            card.getPoints(), card.getLevelCard()))).toList());
        }
        return null;
    }

    @Transactional
    public List<GetGameDTO> getGames() {
        return gameRepository.findAll().stream()
                .map(game -> new GetGameDTO(
                        game.getId(),
                        game.getName(),
                        game.getDescription(),
                        game.getNumberRound(),
                        game.getCards().stream()
                                .map(card -> new GetCardDTO(
                                        card.getId(),
                                        card.getQuestion(),
                                        card.getPoints(),
                                        card.getLevelCard()))
                                .toList()))
                .toList();
    }
}
