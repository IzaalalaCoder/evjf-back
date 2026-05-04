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
    public Integer id;

    public String pseudo;
    public String link;

    @Enumerated(EnumType.STRING)
    public Role role;

    @ManyToOne
    @JoinColumn(name = "lobby_id")
    public Lobby lobby;

    // CONSTRUCTORS

    public Player() {}

    // METHODS

    public Integer getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public String getLink() {
        return link;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
