package com.honeystone.common.dto.board;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.honeystone.board.model.type.Level;
import com.honeystone.board.model.type.Location;
import com.honeystone.board.model.type.Skill;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "비디오 상세 조회용 DTO (파일 정보 포함)")
public class GetBoard {
    @Schema(description = "게시물 인덱스")
    private Long id;

    @Schema(description = "게시물 제목", example = "논현점 파랑이 정복")
    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 50, message = "제목은 50자 이내여야 합니다.")
    private String title;

    @Schema(description = "게시물 상세 내용", example = "오늘 논현점 파랑이 완전 정복했어요!!")
    private String description;

    @Schema(description = "난이도", example = "RED")
    @Enumerated(EnumType.STRING)
    private Level level;
    
    @JsonProperty("korLevel")
    @Schema(description = "난이도 한글명", example = "빨강")
    private String getKorLevel() {
    	return level.getKorName();
    }

    @Schema(description = "기술명", example = "[\"PINCH\", \"SLOPER\"]")
    @Enumerated(EnumType.STRING)
    private Set<Skill> skill;
    
    @JsonProperty("korSkill")
    @Schema(description = "기술 한글명", example = "핀치")
    private List<String> getKorSkill() {
    	List<String> skills = new ArrayList<>();
    	for(Skill s : skill) {
    		skills.add(s.getKorName());
    	}
    	return skills;
    }

    @Schema(description = "홀드 색깔", example = "빨강")
    private String holdColor;

    @Schema(description = "작성자 인덱스")
    private Long userId;

    @Schema(description = "생성 시각", example = "2025-05-11T18:45:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정 시각", example = "2025-05-11T19:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "삭제 시각", example = "2025-05-11T19:00:00")
    private LocalDateTime deletedAt;

    @Schema(description = "파일 인덱스", example = "1")
    private Long fileId;

    @Schema(description = "파일 URL", example = "https://cdn.example.com/videos/1/file.png")
    private String url;

    @Schema(description = "파일명", example = "file.png")
    private String filename;

    @Schema(description = "댓글 수")
    private Long reviewCount;
    
    @Schema(description = "좋아요 수")
    private Long favoriteCount;

    @Schema(description = "장소", example = "HONGDAE")
    @Enumerated(EnumType.STRING)
    private Location location;
    
    @JsonProperty("korLocation")
    @Schema(description = "장소 한글명", example = "홍대")
    private String getKorLocation() {
    	return location.getKorName();
    }

    @Schema(description = "벽", example = "사과")
    private String wall;

    @Schema(description = "유저 닉네임", example = "허니스톤")
    private String nickname;

    @Schema(description = "유저 프로필 이미지", example = "default.png")
    private String profile;

    @Schema(description = "찜 추가 or not", example = "true")
    private boolean liked;

}
