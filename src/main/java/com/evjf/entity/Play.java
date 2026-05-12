package com.evjf.entity;

import com.evjf.enumerate.PlayStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "play")
public class Play {

    // ATTRIBUTES

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "current_card_id")
    private Card currentCard;

    private Integer currentRound = 0;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(50)")
    private PlayStatus status = PlayStatus.IN_PROGRESS;

    @ManyToMany
    @JoinTable(
            name = "play_played_cards",
            joinColumns = @JoinColumn(name = "play_id"),
            inverseJoinColumns = @JoinColumn(name = "card_id")
    )
    private final List<Card> playedCards = new ArrayList<>();

    // METHODS


    public List<Card> getPlayedCards() {
        return playedCards;
    }

    public void addPlayedCard(Card card) {
        playedCards.add(card);
    }

    public void clearPlayedCards() {
        playedCards.clear();
    }

    public PlayStatus getStatus() {
        return status;
    }

    public void setStatus(PlayStatus status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public Card getCurrentCard() {
        return currentCard;
    }
    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
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

}