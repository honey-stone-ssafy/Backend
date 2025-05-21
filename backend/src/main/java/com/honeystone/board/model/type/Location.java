package com.honeystone.board.model.type;

import java.util.Arrays;

public enum Location {

    HONGDAE("홍대"),
    ILSAN("일산"),
    MAGOK("마곡"),
    SEOULDAE("서울대"),
    YANGJAE("양재"),
    SINLIM("신림"),
    YEONNAM("연남"),
    GANGNAM("강남"),
    SADANG("사당"),
    SINSA("신사"),
    NONHYEON("논현"),
    MULLAE("문래"),
    ISU("이수"),
    SUNGSU("성수");

    private final String korName;

    Location(String korName) {
        this.korName = korName;
    }

    public String getKorName() {
        return korName;
    }
    
    public static Location from(String value) {
        return Arrays.stream(Location.values())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("허용되지 않은 Location 값입니다: " + value));
    }
}

