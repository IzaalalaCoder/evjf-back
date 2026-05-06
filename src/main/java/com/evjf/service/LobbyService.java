package com.evjf.service;

import com.evjf.dto.CreatePlayerDTO;
import com.evjf.dto.GetLobbyDTO;
import com.evjf.dto.GetPlayerDTO;
import com.evjf.entity.Game;
import com.evjf.entity.Lobby;
import com.evjf.entity.Player;
import com.evjf.enumerate.Role;
import com.evjf.enumerate.Status;
import com.evjf.repository.GameRepository;
import com.evjf.repository.LobbyRepository;
import com.evjf.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;

@Service
public class LobbyService {

    // CONSTANTS

    private static final String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    // ATTRIBUTES

    private final LobbyRepository lobbyRepository;
    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;

    // CONSTRUCTORS

    public LobbyService(LobbyRepository lobbyRepository,
                        PlayerRepository playerRepository,
                        GameRepository gameRepository) {
        this.lobbyRepository = lobbyRepository;
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
    }

    // METHODS

    public void createLobby() {
        Lobby lobby = new Lobby();
        lobby.setCode(generateUniqueCode());
        lobbyRepository.save(lobby);
    }

    public void launchSession(String code) {
        Lobby lobby = this.lobbyRepository.findById(code).orElse(null);
        if (lobby != null && lobby.getStatus() == Status.WAITING && lobby.getPlayers().size() > 1) {
            if (lobby.getPlayers().stream().filter(e -> e.getRole() == Role.FUTURE_BRIDE).toList().size() == 1) {
                lobby.setStatus(Status.SETUP);
                this.lobbyRepository.save(lobby);
            }
        }
    }

    public void launchGame(String code, Integer gameId) {
         Game game = this.gameRepository.findById(gameId).orElse(null);
         Lobby lobby = this.lobbyRepository.findById(code).orElse(null);
         if (lobby != null && game != null && lobby.getStatus() == Status.SETUP) {
             lobby.setStatus(Status.PLAYING);
             lobby.setGame(game);
             lobby.setCurrentRound(0);
             this.lobbyRepository.save(lobby);
         }
    }

    public void increaseRound(String code) {
        Lobby lobby = this.lobbyRepository.findById(code).orElse(null);
        if (lobby != null && lobby.getStatus() == Status.PLAYING) {
            if (lobby.getCurrentRound() < lobby.getGame().getNumberRound()) {
                lobby.setCurrentRound(lobby.getCurrentRound() + 1);
            } else {
                lobby.setStatus(Status.SETUP);
            }
            this.lobbyRepository.save(lobby);
        }
    }

    public void endGame(String code) {
        Lobby lobby = this.lobbyRepository.findById(code).orElse(null);
        if (lobby != null && lobby.getStatus() == Status.PLAYING) {
            lobby.setStatus(Status.SETUP);
            lobby.setGame(null);
            lobby.setCurrentRound(0);
            this.lobbyRepository.save(lobby);
        }
    }

    public void closeSession(String code) {
        Lobby lobby = this.lobbyRepository.findById(code).orElse(null);
        if (lobby != null && lobby.getStatus() != Status.WAITING) {
            lobby.setStatus(Status.FINISHED);
            lobby.setGame(null);
            lobby.setCurrentRound(0);
            this.lobbyRepository.save(lobby);
        }
    }

    public void addPlayerToLobby(CreatePlayerDTO createPlayerDTO) {
        Lobby lobby = this.lobbyRepository.findById(createPlayerDTO.lobbyCode()).orElse(null);
        if (lobby != null && lobby.getStatus() == Status.WAITING) {
            if (!playerRepository.existsByPseudoAndLobbyCode(createPlayerDTO.pseudo(), createPlayerDTO.lobbyCode())) {
                Player player = new Player();
                player.setLobby(lobby);
                player.setDeviceId(createPlayerDTO.deviceId());
                player.setPseudo(createPlayerDTO.pseudo());
                player.setRole(createPlayerDTO.role());
                lobby.addPlayer(player);
                this.lobbyRepository.save(lobby);
            }
        }
    }

    public void removePlayerFromLobby(String code, Integer playerId) {
        Lobby lobby = lobbyRepository.findById(code).orElse(null);
        Player player = this.playerRepository.findById(playerId).orElse(null);
        if (lobby != null && player != null && lobby.getStatus() == Status.WAITING) {
            lobby.removePlayer(player);
            lobbyRepository.save(lobby);
        }
    }

    public void deleteLobby(String code) {
        lobbyRepository.deleteById(code);
    }

    public GetLobbyDTO getLobbyByCode(String code) {
        Lobby lobby = this.lobbyRepository.findById(code).orElse(null);
        return toDTOObject(lobby);
    }

    // UTILS

    private String generateCode() {
        SecureRandom random = new SecureRandom();
        int lengthCodeMax = 6;
        StringBuilder code = new StringBuilder(lengthCodeMax);
        for (int i = 0; i < lengthCodeMax; i++) {
            code.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return code.toString();
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = generateCode();
        } while (lobbyRepository.existsById(code));
        return code;
    }

    private  GetLobbyDTO toDTOObject(Lobby lobby) {
        if (lobby == null) {
            return null;
        }
        return new GetLobbyDTO(lobby.getCode(),
                lobby.getStatus(), lobby.getPlayers().stream().map(this::toDTOObject).toList());
    }

    private GetPlayerDTO toDTOObject(Player player) {
        if (player == null) {
            return null;
        }
        return new GetPlayerDTO(player.getId(), player.getPseudo(), player.getRole());
    }
}
