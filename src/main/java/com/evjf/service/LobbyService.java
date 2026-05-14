package com.evjf.service;

import com.evjf.dto.*;
import com.evjf.entity.*;
import com.evjf.enumerate.PlayStatus;
import com.evjf.enumerate.Role;
import com.evjf.enumerate.Status;
import com.evjf.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;

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
        if (lobby != null
                && lobby.getStatus() == Status.WAITING
                && lobby.getPlayers().size() > 1
                && lobby.getPlayers().stream().filter(e -> e.getRole() == Role.FUTURE_BRIDE).toList().size() == 1) {
            lobby.setStatus(Status.SETUP);
            this.lobbyRepository.save(lobby);
        }
    }

    @Transactional
    public void launchGame(String code, Integer gameId) {
        Game game = this.gameRepository.findById(gameId).orElse(null);
        Lobby lobby = this.lobbyRepository.findById(code).orElse(null);
        if (lobby != null && game != null && lobby.getStatus() == Status.SETUP && !game.getCards().isEmpty()) {
            Play play = new Play();
            play.setGame(game);
            play.setCurrentRound(0);
            play.setStatus(PlayStatus.IN_PROGRESS);
            play.setCurrentPlayer(lobby.getPlayers().getFirst());
            // Première carte
            Card firstCard = game.getCards().getFirst();
            play.setCurrentCard(firstCard);
            play.addPlayedCard(firstCard);
            playRepository.save(play);
            lobby.setCurrentPlay(play);
            lobby.setStatus(Status.PLAYING);
            this.lobbyRepository.save(lobby);
        }
    }

    @Transactional
    public void play(String code, Boolean done) {
        Lobby lobby = this.lobbyRepository.findById(code).orElse(null);
        if (lobby == null
                || lobby.getStatus() != Status.PLAYING
                || lobby.getCurrentPlay() == null) return;

        Play play = lobby.getCurrentPlay();

        // 1. Ajouter les points si la carte a été réussie
        if (Boolean.TRUE.equals(done) && play.getCurrentCard() != null) {
            play.getCurrentPlayer().addPoints(play.getCurrentCard().getPoints());
            playerRepository.save(play.getCurrentPlayer());
        }

        // 2. Calculer le nombre de questions jouées dans le round courant
        //    (currentCard est déjà dans playedCards depuis launchGame/play précédent)
        int questionsInCurrentRound = play.getPlayedCards().size()
                - (play.getCurrentRound() * play.getGame().getNumberQuestionsByRound());

        boolean roundComplete = questionsInCurrentRound == play.getGame().getNumberQuestionsByRound();

        if (roundComplete) {
            // Round terminé
            if (Objects.equals(play.getCurrentRound(), play.getGame().getNumberRound())) {
                // Plus de rounds → fin du jeu automatique
                play.setStatus(PlayStatus.FINISHED);
                lobby.setCurrentPlay(null);
                lobby.setStatus(Status.SETUP);
                playRepository.save(play);
                lobbyRepository.save(lobby);
                return;
            } else {
                // Passer au round suivant
                play.setCurrentRound(play.getCurrentRound() + 1);
                findAndSetNextCard(play);
            }
        } else {
            // Encore des questions dans ce round → carte suivante
            findAndSetNextCard(play);
        }

        // 3. Passer au joueur suivant
        List<Player> players = lobby.getPlayers();
        int index = players.indexOf(play.getCurrentPlayer());
        index = index < players.size() - 1 ? index + 1 : 0;
        play.setCurrentPlayer(players.get(index));

        playRepository.save(play);
        lobbyRepository.save(lobby);
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
        if (lobby != null
                && lobby.getStatus() != Status.WAITING
                && lobby.getStatus() != Status.FINISHED) {
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

    private void findAndSetNextCard(Play play) {
        play.getGame().getCards().stream()
                .filter(c -> !play.getPlayedCards().contains(c))
                .findFirst()
                .ifPresentOrElse(
                        nextCard -> {
                            play.setCurrentCard(nextCard);
                            play.addPlayedCard(nextCard);
                        },
                        () -> play.setCurrentCard(null)
                );
    }

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

    private GetLobbyDTO toDTOObject(Lobby lobby) {
        if (lobby == null) return null;

        GetPlayDTO playDTO = null;
        if (lobby.getCurrentPlay() != null) {
            Play play = lobby.getCurrentPlay();

            // Nombre de questions jouées dans le round courant
            int questionsInCurrentRound = play.getPlayedCards().size()
                    - (play.getCurrentRound() * play.getGame().getNumberQuestionsByRound());

            GetCardDTO cardDTO = play.getCurrentCard() != null
                    ? new GetCardDTO(
                    play.getCurrentCard().getId(),
                    play.getCurrentCard().getQuestion(),
                    play.getCurrentCard().getPoints(),
                    play.getCurrentCard().getLevelCard())
                    : null;

            GetPlayerDTO currentPlayerDTO = play.getCurrentPlayer() != null
                    ? toDTOObject(play.getCurrentPlayer())
                    : null;

            playDTO = new GetPlayDTO(
                    play.getId(),
                    new GetGameDTO(
                            play.getGame().getId(),
                            play.getGame().getName(),
                            play.getGame().getDescription(),
                            play.getGame().getNumberRound(),
                            null),
                    cardDTO,
                    play.getCurrentRound(),
                    questionsInCurrentRound,
                    play.getStatus(),
                    currentPlayerDTO
            );
        }

        return new GetLobbyDTO(
                lobby.getCode(),
                lobby.getStatus(),
                lobby.getPlayers().stream().map(this::toDTOObject).toList(),
                playDTO
        );
    }

    private GetPlayerDTO toDTOObject(Player player) {
        if (player == null) return null;
        return new GetPlayerDTO(player.getId(), player.getPseudo(), player.getRole(), player.getPoints());
    }
}