package com.honeystone.board.model.type;

import java.util.Arrays;

public enum Level {
    WHITE("하양"),
    YELLOW("노랑"),
    ORANGE("주황"),
    GREEN("초록"),
    BLUE("파랑"),
    RED("빨강"),
    PURPLE("보라"),
    GREY("회색"),
    BROWN("갈색"),
    BLACK("검정");

    private final String korName;

    Level(String korName) {
        this.korName = korName;
    }

    public String getKorName() {
        return korName;
    }
    
    public static Level from(String value) {
        return Arrays.stream(Level.values())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("허용되지 않은 Level 값입니다: " + value));
    }
}
