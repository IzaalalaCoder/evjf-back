package com.evjf.service;

import com.evjf.dto.*;
import com.evjf.entity.Game;
import com.evjf.entity.Lobby;
import com.evjf.entity.Play;
import com.evjf.entity.Player;
import com.evjf.enumerate.PlayStatus;
import com.evjf.enumerate.Role;
import com.evjf.enumerate.Status;
import com.evjf.repository.GameRepository;
import com.evjf.repository.LobbyRepository;
import com.evjf.repository.PlayRepository;
import com.evjf.repository.PlayerRepository;
import jakarta.transaction.Transactional;
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
    private final PlayRepository playRepository;

    // CONSTRUCTORS

    public LobbyService(LobbyRepository lobbyRepository,
                        PlayerRepository playerRepository,
                        GameRepository gameRepository,
                        PlayRepository playRepository) {
        this.lobbyRepository = lobbyRepository;
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.playRepository = playRepository;
    }

    // METHODS

    public void createLobby() {
        Lobby lobby = new Lobby(generateUniqueCode());
        lobbyRepository.save(lobby);
    }

    @Transactional
    public void launchSession(String code) {
        Lobby lobby = this.lobbyRepository.findById(code).orElse(null);
        if (lobby != null && lobby.getStatus() == Status.WAITING && lobby.getPlayers().size() > 1) {
            if (lobby.getPlayers().stream().filter(e -> e.getRole() == Role.FUTURE_BRIDE).toList().size() == 1) {
                lobby.setStatus(Status.SETUP);
                this.lobbyRepository.save(lobby);
            }
        }
    }

    @Transactional
    public void launchGame(String code, Integer gameId) {
        Game game = this.gameRepository.findById(gameId).orElse(null);
        Lobby lobby = this.lobbyRepository.findById(code).orElse(null);
        if (lobby != null && game != null && lobby.getStatus() == Status.SETUP) {
            Play play = new Play();
            play.setGame(game);
            play.setCurrentRound(0);
            play.setStatus(PlayStatus.IN_PROGRESS);
            // Première carte
            if (!game.getCards().isEmpty()) {
                play.setCurrentCard(game.getCards().getFirst());
                play.getPlayedCards().add(game.getCards().getFirst());
            }
            playRepository.save(play);
            lobby.setCurrentPlay(play);
            lobby.setStatus(Status.PLAYING);
            this.lobbyRepository.save(lobby);
        }
    }

    @Transactional
    public void increaseRound(String code) {
        Lobby lobby = this.lobbyRepository.findById(code).orElse(null);
        if (lobby != null && lobby.getStatus() == Status.PLAYING && lobby.getCurrentPlay() != null) {
            Play play = lobby.getCurrentPlay();
            if (play.getCurrentRound() < play.getGame().getNumberRound()) {
                play.setCurrentRound(play.getCurrentRound() + 1);
                // Prochaine carte non jouée
                play.getGame().getCards().stream()
                        .filter(c -> !play.getPlayedCards().contains(c))
                        .findFirst()
                        .ifPresent(nextCard -> {
                            play.setCurrentCard(nextCard);
                            play.getPlayedCards().add(nextCard);
                        });
            } else {
                play.setStatus(PlayStatus.FINISHED);
                lobby.setStatus(Status.SETUP);
                lobby.setCurrentPlay(null);
            }
            playRepository.save(play);
            this.lobbyRepository.save(lobby);
        }
    }

    @Transactional
    public void endGame(String code) {
        Lobby lobby = this.lobbyRepository.findById(code).orElse(null);
        if (lobby != null && lobby.getStatus() == Status.PLAYING && lobby.getCurrentPlay() != null) {
            Play play = lobby.getCurrentPlay();
            play.setStatus(PlayStatus.FINISHED);
            playRepository.save(play);
            lobby.setCurrentPlay(null);
            lobby.setStatus(Status.SETUP);
            this.lobbyRepository.save(lobby);
        }
    }

    @Transactional
    public void closeSession(String code) {
        Lobby lobby = this.lobbyRepository.findById(code).orElse(null);
        if (lobby != null && lobby.getStatus() != Status.WAITING) {
            if (lobby.getCurrentPlay() != null) {
                Play play = lobby.getCurrentPlay();
                play.setStatus(PlayStatus.FINISHED);
                playRepository.save(play);
                lobby.setCurrentPlay(null);
            }
            lobby.setStatus(Status.FINISHED);
            this.lobbyRepository.save(lobby);
        }
    }

    @Transactional
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

    @Transactional
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

    @Transactional
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
        GetPlayDTO playDTO = null;
        if (lobby.getCurrentPlay() != null) {
            Play play = lobby.getCurrentPlay();
            playDTO = new GetPlayDTO(
                    play.getId(),
                    // game DTO
                    new GetGameDTO(play.getGame().getId(), play.getGame().getName(),
                            play.getGame().getDescription(), play.getGame().getNumberRound(), null),
                    // current card DTO
                    play.getCurrentCard() != null ? new GetCardDTO(play.getCurrentCard().getId(),
                            play.getCurrentCard().getQuestion(), play.getCurrentCard().getPoints(),
                            play.getCurrentCard().getLevel()) : null,
                    play.getCurrentRound(),
                    play.getStatus()
            );
        }
        return new GetLobbyDTO(lobby.getCode(),
                lobby.getStatus(), lobby.getPlayers().stream().map(this::toDTOObject).toList(), playDTO);
    }

    private GetPlayerDTO toDTOObject(Player player) {
        if (player == null) {
            return null;
        }
        return new GetPlayerDTO(player.getId(), player.getPseudo(), player.getRole());
    }
}
