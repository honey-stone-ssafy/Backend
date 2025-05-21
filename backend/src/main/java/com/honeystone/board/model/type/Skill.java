package com.honeystone.board.model.type;

import java.util.Arrays;

public enum Skill {
    PINCH("핀치"),
    SLOPER("슬로퍼"),
    CRIMP("크림프"),
    POCKET_HOLD("포켓홀드"),
    COORDINATION("코디"),
    LUNGE("런지"),
    DYNO("다이노"),
    BALANCE("밸런스"),
    OVERHANG("오버행"),
    TOE_HOOK("토훅"),
    HEEL_HOOK("힐훅"),
    BAT_HANG("배트행"),
    COUNTER_BALANCING("카운터밸런싱"),
    DEADPOINT("데드포인트"),
    POGO("포고"),
    FLIP("플립"),
    KNEEBAR("니바"),
    DROP_KNEE("드롭니"),
    PUSH("푸쉬"),
    RUN_AND_JUMP("런앤점프"),
    CAMPUSING("캠퍼싱"),
    TOE_CATCH("토캐치");

    private final String korName;

    Skill(String korName) {
        this.korName = korName;
    }

    public String getKorName() {
        return korName;
    }
    
    public static Skill from(String value) {
        return Arrays.stream(Skill.values())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("허용되지 않은 Skill 값입니다: " + value));
    }
}
