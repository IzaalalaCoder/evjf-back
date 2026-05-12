package com.evjf.entity;

import com.evjf.enumerate.Status;
import jakarta.persistence.*;

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
    @JoinColumn(name = "play_id")
    private Play currentPlay;

    @Id
    @Column(unique = true, nullable = false)
    private String code;

    // CONSTRUCTORS

    public Lobby() {}

    public Lobby(String code) {
        this.code = code;
    }

    // METHODS

    public String getCode() {
        return code;
    }

    public Play getCurrentPlay() {
        return currentPlay;
    }

    public void setCurrentPlay(Play play) {
        this.currentPlay = play;
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
