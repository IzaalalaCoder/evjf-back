package com.evjf.dto;

import com.evjf.enumerate.Level;

public record GetCardDTO(Integer id, String question, Integer points, Level level) {}