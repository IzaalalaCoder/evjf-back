package com.evjf.controller;

import com.evjf.dto.GetGameDTO;
import com.evjf.service.GameService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/game")
@CrossOrigin(origins = "*")
public class GameController {

    // ATTRIBUTES

    private final GameService gameService;

    // CONSTRUCTORS

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // METHODS

    @GetMapping(path = "{id}")
    public GetGameDTO getGameById(@PathVariable Integer id) {
        return gameService.getGame(id);
    }

    @GetMapping()
    public List<GetGameDTO> getGames() {
        return gameService.getGames();
    }

}