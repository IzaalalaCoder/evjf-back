package com.evjf.entity;

import com.evjf.enumerate.PlayStatus;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "current_player_id")
    private Player currentPlayer;

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
    private List<Card> playedCards = new ArrayList<>();

    // CONSTRUCTORS

    public Play() {}

    // METHODS

    public void addPlayedCard(Card card) {
        this.playedCards.add(card);
    }

    public Integer getId() { return id; }

    public Game getGame() { return game; }
    public void setGame(Game game) { this.game = game; }

    public Card getCurrentCard() { return currentCard; }
    public void setCurrentCard(Card currentCard) { this.currentCard = currentCard; }

    public Player getCurrentPlayer() { return currentPlayer; }
    public void setCurrentPlayer(Player currentPlayer) { this.currentPlayer = currentPlayer; }

    public Integer getCurrentRound() { return currentRound; }
    public void setCurrentRound(Integer currentRound) { this.currentRound = currentRound; }

    public PlayStatus getStatus() { return status; }
    public void setStatus(PlayStatus status) { this.status = status; }

    public List<Card> getPlayedCards() { return playedCards; }
}