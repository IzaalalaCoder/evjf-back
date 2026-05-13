package com.evjf.entity;

import com.evjf.enumerate.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.EnumType;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Table(name = "player")
public class Player {

    // ATTRIBUTES

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String pseudo;
    private String deviceId;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "lobby_code")
    private Lobby lobby;

    private Integer points = 0;

    // CONSTRUCTORS

    public Player() {}

    // METHODS

    public Integer getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void addPoints(Integer points) {
        this.points += points;
    }
}
