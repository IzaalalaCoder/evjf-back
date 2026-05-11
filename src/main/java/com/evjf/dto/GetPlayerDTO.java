package com.evjf.dto;

import com.evjf.enumerate.Role;

public record GetPlayerDTO(Integer id, String pseudo, Role role) {}
