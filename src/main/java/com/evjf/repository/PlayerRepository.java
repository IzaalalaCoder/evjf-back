package com.evjf.repository;

import com.evjf.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player,Integer> {
    public Boolean existsByPseudoAndLobbyCode(String pseudo, String lobbyCode);
}
