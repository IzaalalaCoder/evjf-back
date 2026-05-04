package com.evjf.repository;

import com.evjf.entity.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LobbyRepository extends JpaRepository<Lobby,Integer> {
    Boolean existsByCode(String code);
    Optional<Lobby> findByCode(String code);
}
