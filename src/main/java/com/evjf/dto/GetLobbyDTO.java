package com.evjf.dto;

import com.evjf.enumerate.Status;
import java.util.List;

public record GetLobbyDTO(Integer id, Status status, List<GetPlayerDTO> playerDTOs) {
}
