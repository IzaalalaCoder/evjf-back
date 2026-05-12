package com.evjf.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "game")
public class Game {
    // ATTRIBUTES

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;
    private Integer numberRound;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game", orphanRemoval = true)
    private List<Card> cards = new ArrayList<>();

    // CONSTRUCTORS

    public Game() {}

    // METHODS

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getNumberRound() {
        return numberRound;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNumberRound(Integer numberRound) {
        this.numberRound = numberRound;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
