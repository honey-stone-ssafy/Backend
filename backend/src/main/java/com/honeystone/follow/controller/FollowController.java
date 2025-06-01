package com.honeystone.follow.controller;

import com.honeystone.common.dto.error.ApiError;
import com.honeystone.common.security.MyUserPrincipal;
import com.honeystone.follow.model.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follow/{userId}")
@Tag(name= "Follow API", description = "팔로우 관련 API 입니다.")
public class FollowController {

    private final FollowService followService;
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("")
    @Operation(summary = "팔로우 추가", description = "새로운 유저를 팔로우합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "팔로우 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = ApiError.class)))
            })
    public ResponseEntity<?> addFollow(@AuthenticationPrincipal MyUserPrincipal user, @PathVariable("userId") Long followingUserId) {
        followService.addFollow(user, followingUserId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @DeleteMapping("")
    @Operation(summary = "팔로우 삭제", description = "유저 팔로잉을 취소합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "팔로우 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = ApiError.class)))
            })
    public ResponseEntity<?> deleteFollow(@AuthenticationPrincipal MyUserPrincipal user, @PathVariable("userId") Long followingUserId) {
        followService.removeFollow(user, followingUserId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
