package com.evjf.dto;

import java.util.List;

public record GetGameDTO(Integer id, String name, String description, Integer numberRound, List<GetCardDTO> cardDTOs) {
}
