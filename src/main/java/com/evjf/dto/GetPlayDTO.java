package com.evjf.dto;

import com.evjf.enumerate.PlayStatus;

public record GetPlayDTO(
        Integer id,
        GetGameDTO game,
        GetCardDTO currentCard,
        Integer currentRound,
        Integer currentNumberQuestion,
        PlayStatus status,
        GetPlayerDTO currentPlayer
) {}