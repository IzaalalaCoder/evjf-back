package com.evjf.dto;

import com.evjf.enumerate.PlayStatus;

public record GetPlayDTO(
        Integer id,
        GetGameDTO game,
        GetCardDTO currentCard,
        Integer currentRound,
        PlayStatus status
) {}