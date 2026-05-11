package com.evjf.dto;

import com.evjf.enumerate.Role;

public record CreatePlayerDTO(String pseudo, String deviceId, Role role, String lobbyCode) {}
