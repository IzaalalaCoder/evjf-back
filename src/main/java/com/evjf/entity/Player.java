package com.evjf.entity;

import com.evjf.enumerate.Rule;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.EnumType;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.springframework.data.annotation.Id;


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
    public Rule rule;

    @ManyToOne
    @JoinColumn(name = "loby_id")
    public Loby loby;

    // CONSTRUCTORS

    public Player() {}

    // METHODS

    public Integer getId() {
        return id;
    }

    public Rule getRule() {
        return rule;
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

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
