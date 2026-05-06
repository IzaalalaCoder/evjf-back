package com.evjf.controller;

import com.evjf.dto.CreatePlayerDTO;
import com.evjf.dto.GetLobbyDTO;
import com.evjf.service.LobbyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lobby")
@CrossOrigin(origins = "*")
public class LobbyController {

    // ATTRIBUTES

    private final LobbyService lobbyService;

    // CONSTRUCTORS

    public LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    // METHODS

    @PostMapping
    public void createLobby() {
        this.lobbyService.createLobby();
    }

    @PutMapping(path = "{code}/launch")
    public void launchSession(@PathVariable String code) {
        this.lobbyService.launchSession(code);
    }

    @PutMapping(path = "{code}/game/{gameId}")
    public void chooseGame(@PathVariable String code, @PathVariable Integer gameId) {
        this.lobbyService.launchGame(code, gameId);
    }

    @PutMapping(path = "{code}/endgame")
    public void endGame(@PathVariable String code) {
        this.lobbyService.endGame(code);
    }

    @PutMapping(path = "{code}/close")
    public void closeSession(@PathVariable String code) {
        this.lobbyService.closeSession(code);
    }

    @DeleteMapping(path = "{code}")
    public void deleteLobby(@PathVariable String code) {
        this.lobbyService.deleteLobby(code);
    }

    @PostMapping(path = "join")
    public void joinLobby(@RequestBody CreatePlayerDTO createPlayerDTO) {
        this.lobbyService.addPlayerToLobby(createPlayerDTO);
    }

    @PutMapping(path = "{code}/player/{playerId}")
    public void leaveLobby(@PathVariable String code, @PathVariable Integer playerId) {
        this.lobbyService.removePlayerFromLobby(code, playerId);
    }

    @GetMapping(path = "{code}")
    public GetLobbyDTO getLobbyByCode(@PathVariable String code) {
        return this.lobbyService.getLobbyByCode(code);
    }


}
