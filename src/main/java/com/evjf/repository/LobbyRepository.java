package com.evjf.repository;

import com.evjf.entity.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LobbyRepository extends JpaRepository<Lobby,Integer> {
}
