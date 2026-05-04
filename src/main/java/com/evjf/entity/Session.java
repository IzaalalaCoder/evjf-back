package com.evjf.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
@Table(name = "session")
public class Session {
    // ATTRIBUTES

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    public Integer currentRound;

    @OneToOne
    @JoinColumn(name = "lobby_id")
    public Lobby lobby;

    @OneToOne
    @JoinColumn(name = "game_id")
    public Game game;

    // CONSTRUCTOR

    public Session() {}

    // METHODS


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(Integer currentRound) {
        this.currentRound = currentRound;
    }

    public Lobby getLoby() {
        return lobby;
    }

    public void setLoby(Lobby loby) {
        this.lobby = loby;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

}
