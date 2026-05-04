package com.evjf.enumerate;

public enum Level {
    SOFT("Chelou"),
    NORMAL("Attention ça se corse"),
    HARD("Oh putain cours");

    private final String name;

    Level(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
