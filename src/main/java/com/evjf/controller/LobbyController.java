package com.evjf.controller;

import com.evjf.dto.CreatePlayerDTO;
import com.evjf.dto.GetLobbyDTO;
import com.evjf.service.LobbyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

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

    @PutMapping(path = "{code}/play")
    public void play(@PathVariable String code, @RequestBody Boolean done) {
        this.lobbyService.play(code, done);
    }

    @PostMapping
    public ResponseEntity<Void> createLobby() {
        this.lobbyService.createLobby();
        return ResponseEntity.status(201).build();
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
    public ResponseEntity<GetLobbyDTO> getLobbyByCode(@PathVariable String code) {
        GetLobbyDTO lobbyDTO = this.lobbyService.getLobbyByCode(code);
        if (lobbyDTO == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(lobbyDTO);
    }
}
