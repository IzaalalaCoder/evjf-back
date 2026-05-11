package com.evjf.entity;

import com.evjf.enumerate.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Id;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lobby")
public class Lobby {

    // ATTRIBUTES

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "VARCHAR(50)")
    private Status status = Status.WAITING;

    @OneToMany(mappedBy = "lobby", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Player> players = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "game_id")
    private Game game = null;

    private Integer currentRound = 0;

    @Id
    @Column(unique = true, nullable = false)
    private String code;

    // CONSTRUCTORS

    public Lobby() {}

    // METHODS

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Integer getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(Integer currentRound) {
        this.currentRound = currentRound;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
        player.setLobby(this);
    }

    public void removePlayer(Player player) {
        player.setLobby(null);
        this.players.remove(player);
    }
}
