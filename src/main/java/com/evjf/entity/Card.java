package com.evjf.entity;

import com.evjf.enumerate.Level;
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
@Table(name = "card")
public class Card {

    // ATTRIBUTES

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String question;
    private Integer points;

    @Enumerated(EnumType.STRING)
    private Level level;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    // CONSTRUCTORS

    public Card() {}

    // METHODS

    public Integer getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public Integer getPoints() {
        return points;
    }

    public Level getLevel() {
        return level;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
