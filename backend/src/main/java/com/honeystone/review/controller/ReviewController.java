package com.honeystone.review.controller;

import com.honeystone.common.security.MyUserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.honeystone.common.dto.review.Review;
import com.honeystone.review.model.service.ReviewService;

@RestController
@RequestMapping("/api/boards/{boardId}/reviews")
@Tag(name= "Review API", description = "게시물 리뷰 관련 API 입니다.")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "리뷰 목록 조회", description = """
                특정 영상(`boardId`)에 대한 모든 리뷰를 조회합니다.\n
                사용자 인증 후 접근 가능합니다.\n
                default로 한 페이지당 10개의 데이터를 가져옵니다. page, size를 원하는 수로 작성하여 테스트할 수 있습니다.
            """, responses = { @ApiResponse(responseCode = "200", description = "리뷰 목록 조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류") })
    @GetMapping()
    public ResponseEntity<Page<Review>> getReviewList(@PathVariable("boardId") Long boardId,
                                                      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        // 페이지네이션
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviews = reviewService.getReviewList(boardId, pageable);

        return new ResponseEntity<Page<Review>>(reviews, HttpStatus.OK);
    }

    @Operation(summary = "리뷰 작성", description = """
            리뷰 내용을 JSON 형식으로 전송하여 새로운 리뷰를 등록합니다.\n
            `boardId`는 PathVariable로 전달하며, `Review` 객체의 `id`, `createdAt`, `updatedAt`, `deletedAt` 필드는 비워두어야 합니다.
        """,
        responses = {
            @ApiResponse(responseCode = "200", description = "리뷰 작성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
        }
    )
    @PostMapping()
    public ResponseEntity<Void> createReview(@AuthenticationPrincipal MyUserPrincipal user, @PathVariable("boardId") Long boardId, @RequestBody Review review){
        reviewService.createReview(user.getEmail(), boardId, review);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "리뷰 수정", description = """
            특정 영상(`boardId`)에 대한 특정 리뷰(`reviewId`)를 수정합니다.\n
            요청 본문에는 수정할 리뷰 내용만 포함시키면 됩니다.\n
            작성자 본인만 수정 가능하며, 사용자 인증이 필요합니다.
        """,
        responses = {
            @ApiResponse(responseCode = "200", description = "리뷰 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "수정 권한 없음"),
            @ApiResponse(responseCode = "404", description = "리뷰 또는 영상이 존재하지 않음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
        }
    )
    @PatchMapping("/{reviewId}")
    public ResponseEntity<Void> updateReview(@AuthenticationPrincipal MyUserPrincipal user, @PathVariable("boardId") Long boardId, @PathVariable("reviewId") Long reviewId, @RequestBody Review review){
        reviewService.updateReview(user.getEmail(), boardId, reviewId, review);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "리뷰 삭제", description = """
            특정 영상(`boardId`)에 대한 특정 리뷰(`reviewId`)를 삭제합니다.\n
            작성자 본인만 삭제할 수 있으며, 사용자 인증이 필요합니다.
        """,
        responses = {
            @ApiResponse(responseCode = "200", description = "리뷰 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "삭제 권한 없음"),
            @ApiResponse(responseCode = "404", description = "리뷰 또는 영상이 존재하지 않음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
        }
    )
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@AuthenticationPrincipal MyUserPrincipal user, @PathVariable("boardId") Long boardId, @PathVariable("reviewId") Long reviewId){
        //Authentication으로 사용자 받아와야 함.
        reviewService.deleteReview(user.getEmail(), boardId, reviewId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
