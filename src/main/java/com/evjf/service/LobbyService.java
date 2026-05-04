package com.evjf.service;

import com.evjf.dto.GetLobbyDTO;
import com.evjf.dto.GetPlayerDTO;
import com.evjf.entity.Lobby;
import com.evjf.entity.Player;
import com.evjf.repository.LobbyRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class LobbyService {

    // ATTRIBUTES

    private final LobbyRepository lobbyRepository;

    // CONSTRUCTORS

    public LobbyService(LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }

    // METHODS

    public void createLobby() {
        Lobby lobby = new Lobby();
        lobby.setCode(generateUniqueCode());
        lobbyRepository.save(lobby);
    }

    public GetLobbyDTO getLobbyByCode(String code) {
        Lobby lobby = this.lobbyRepository.findByCode(code).orElse(null);
        if (lobby == null) {
            return null;
        }

        return toDTOObject(lobby);
    }

    public GetLobbyDTO getLobbyById(Integer id) {
        Lobby lobby = this.lobbyRepository.findById(id).orElse(null);
        if (lobby == null) {
            return null;
        }

        return toDTOObject(lobby);
    }

    // UTILS

    private String generateCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(6);
        for (int i = 0; i < 10; i++) {
            String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
            code.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return code.toString();
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = generateCode();
        } while (lobbyRepository.existsByCode(code));
        return code;
    }

    private  GetLobbyDTO toDTOObject(Lobby lobby) {
        return new GetLobbyDTO(lobby.getId(),
                lobby.getStatus(), lobby.getPlayers().stream().map(this::toDTOObject).toList());
    }

    private GetPlayerDTO toDTOObject(Player player) {
        return new GetPlayerDTO(player.getId(), player.getPseudo(), player.getRole());
    }
}
