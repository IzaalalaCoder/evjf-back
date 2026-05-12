package com.evjf.dto;

import com.evjf.enumerate.Status;
import java.util.List;

public record GetLobbyDTO(String code, Status status, List<GetPlayerDTO> playerDTOs, GetPlayDTO currentPlay) {
}
