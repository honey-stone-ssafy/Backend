package com.honeystone.board.model.type;

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
}
