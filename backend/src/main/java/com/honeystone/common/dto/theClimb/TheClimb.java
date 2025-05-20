package com.honeystone.common.dto.theClimb;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.honeystone.board.model.type.Level;
import com.honeystone.board.model.type.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "더 클라임 DTO입니다.")
public class TheClimb {
    @Schema(description = "더 클라임 인덱스")
    private Long id;

    @Schema(description = "지점", example = "HONGDAE")
    @NotNull(message = "지점은 필수입니다.")
    @Enumerated(EnumType.STRING)
    private Location name;

    @Schema(description = "벽 이름", example = "사과")
    private String wall;

    @Override
    public String toString() {
        return "TheClimb{" +
            "id=" + id +
            ", name=" + name +
            ", wall='" + wall + '\'' +
            '}';
    }
}
