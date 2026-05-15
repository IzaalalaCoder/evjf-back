package com.evjf.enumerate;

public enum Level {
    SOFT("Chelou", 10),
    NORMAL("Attention ça se corse", 20),
    HARD("Oh putain cours", 30);

    private final String name;
    private final Integer points;

    Level(String name, Integer points) {
        this.points = points;
        this.name = name;
    }

    public Integer getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }
}
