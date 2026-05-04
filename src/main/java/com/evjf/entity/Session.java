package com.evjf.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Entity;
import org.springframework.data.annotation.Id;

@Entity
@Table(name = "session")
public class Session {
    // ATTRIBUTES

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    public Integer currentRound;

    @JoinColumn(name = "loby_id")
    public Loby loby;

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

    public Loby getLoby() {
        return loby;
    }

    public void setLoby(Loby loby) {
        this.loby = loby;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

}
