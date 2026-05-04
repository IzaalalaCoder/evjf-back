package com.evjf.dto;

import com.evjf.enumerate.Level;

public record GetCardDTO(int id, String question, String points, Level level) {}