package com.honeystone.plan.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.honeystone.common.dto.plan.Plan;
import com.honeystone.common.security.MyUserPrincipal;
import com.honeystone.plan.model.service.PlanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/plans")
@Tag(name= "Plan API", description = "일정 관련 API 입니다.")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @Operation(summary = "사용자 일정 목록 조회", description = """
            로그인한 사용자의 일정(Plan) 목록을 조회합니다.

            🔐 **인증 필요**  
            요청 시 Authorization 헤더에 JWT 토큰을 `Bearer {token}` 형식으로 포함해야 합니다.
        """,
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(responseCode = "200", description = "일정 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
        }
    )
    @GetMapping()
    public ResponseEntity<List<Plan>> getPlanList(@AuthenticationPrincipal MyUserPrincipal user){
        List<Plan> plans = planService.getPlanList(user.getId());
        return new ResponseEntity<List<Plan>>(plans, HttpStatus.OK);
    }

    @Operation(summary = "단일 일정 상세 조회", description = """
            로그인한 사용자의 특정 일정(Plan) 정보를 조회합니다.\n
            `id`는 조회할 일정의 고유 식별자입니다.

            🔐 **인증 필요**  
            요청 시 Authorization 헤더에 JWT 토큰을 `Bearer {token}` 형식으로 포함해야 합니다.
        """,
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(responseCode = "200", description = "일정 상세 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 일정이 존재하지 않음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Plan> getPlan(@AuthenticationPrincipal MyUserPrincipal user, @PathVariable("id") Long id){

        Plan plan = planService.getPlan(user.getId(), id);
        return new ResponseEntity<>(plan, HttpStatus.OK);
    }

    @Operation(summary = "일정 생성", description = """
            로그인한 사용자가 새로운 일정(Plan)을 등록합니다.\n
            요청 본문에 일정 정보를 JSON 형식으로 전달해야 하며,\n
            `id`, `createdAt`, `updatedAt`, `deletedAt`, `userId` 필드는 비워두어야 합니다.

            🔐 **인증 필요**  
            요청 시 Authorization 헤더에 JWT 토큰을 `Bearer {token}` 형식으로 포함해야 합니다.
        """,
        security = @SecurityRequirement(name = "bearerAuth"),
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "생성할 일정 정보 (Plan)",
            required = true,
            content = @Content(schema = @Schema(implementation = Plan.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "일정 생성 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청 본문"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
        }
    )
    @PostMapping("")
    public ResponseEntity<Void> createPlan(@AuthenticationPrincipal MyUserPrincipal user, @Valid @RequestBody Plan plan) {

        planService.createPlan(user.getId(), plan);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    
    @Operation(summary = "일정 수정", description = """
            로그인한 사용자가 기존 일정(Plan)을 수정합니다.\n
            `id`는 수정할 일정의 고유 식별자이며, 요청 본문에는 수정할 내용을 JSON 형식으로 전달합니다.\n
            `id`, `createdAt`, `updatedAt`, `deletedAt`, `userId` 필드는 서버에서 처리하므로 비워두어야 합니다.

            🔐 **인증 필요**  
            요청 시 Authorization 헤더에 JWT 토큰을 `Bearer {token}` 형식으로 포함해야 합니다.
        """,
        security = @SecurityRequirement(name = "bearerAuth"),
        parameters = {
            @Parameter(name = "id", description = "수정할 일정의 ID", example = "1", required = true)
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "수정할 일정 정보 (Plan)",
            required = true,
            content = @Content(schema = @Schema(implementation = Plan.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "일정 수정 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청 데이터"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "수정할 일정이 존재하지 않음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
        }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePlan(@AuthenticationPrincipal MyUserPrincipal user, @PathVariable("id") Long id, @RequestBody Plan plan){

        System.out.println(plan);
        planService.updatePlan(user.getId(), id, plan);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "일정 삭제", description = """
            로그인한 사용자가 특정 일정(Plan)을 삭제합니다.\n
            `id`는 삭제할 일정의 고유 식별자입니다.\n
            삭제는 논리 삭제로 처리될 수 있습니다.

            🔐 **인증 필요**  
            요청 시 Authorization 헤더에 JWT 토큰을 `Bearer {token}` 형식으로 포함해야 합니다.
        """,
        security = @SecurityRequirement(name = "bearerAuth"),
        parameters = {
            @Parameter(name = "id", description = "삭제할 일정의 ID", example = "1", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "일정 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 일정이 존재하지 않음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@AuthenticationPrincipal MyUserPrincipal user, @PathVariable("id") Long id){

        planService.deletePlan(user.getId(), id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
