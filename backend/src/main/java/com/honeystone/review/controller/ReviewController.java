package com.honeystone.review.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.honeystone.common.dto.Review;
import com.honeystone.review.model.service.ReviewService;

@RestController
@RequestMapping("/api/videos/{videoId}/reviews")
public class ReviewController {

	private final ReviewService reviewService;
	
	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}

	@Operation(summary = "리뷰 목록 조회", description = """
			특정 영상(`videoId`)에 대한 모든 리뷰를 조회합니다.\n
			사용자 인증 후 접근 가능합니다.
		""",
		responses = {
			@ApiResponse(responseCode = "200", description = "리뷰 목록 조회 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청"),
			@ApiResponse(responseCode = "500", description = "서버 내부 오류")
		}
	)
	@GetMapping()
	public ResponseEntity<List<Review>> getReviewList(@PathVariable("videoId") Long videoId){
		//Authentication으로 사용자 받아와야 함.
		List<Review> reviews = reviewService.getReviewList(videoId);
		
		return new ResponseEntity<List<Review>>(reviews, HttpStatus.OK);
	}

	@Operation(summary = "리뷰 작성", description = """
			리뷰 내용을 JSON 형식으로 전송하여 새로운 리뷰를 등록합니다.\n
			`videoId`는 PathVariable로 전달하며, `Review` 객체의 `id`, `createdAt`, `updatedAt`, `deletedAt` 필드는 비워두어야 합니다.
		""",
		responses = {
			@ApiResponse(responseCode = "200", description = "리뷰 작성 성공"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청"),
			@ApiResponse(responseCode = "500", description = "서버 내부 오류")
		}
	)
	@PostMapping()
	public ResponseEntity<Void> createReview(@PathVariable("videoId") Long videoId, @RequestBody Review review){
		//Authentication으로 사용자 받아와야 함.
		reviewService.createReview(videoId, review);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Operation(summary = "리뷰 수정", description = """
			특정 영상(`videoId`)에 대한 특정 리뷰(`reviewId`)를 수정합니다.\n
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
	public ResponseEntity<Void> updateReview(@PathVariable("videoId") Long videoId, @PathVariable("reviewId") Long reviewId, @RequestBody Review review){
		//Authentication으로 사용자 받아와야 함.
		reviewService.updateReview(videoId, reviewId, review);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Operation(summary = "리뷰 삭제", description = """
			특정 영상(`videoId`)에 대한 특정 리뷰(`reviewId`)를 삭제합니다.\n
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
	public ResponseEntity<Void> deleteReview(@PathVariable("videoId") Long videoId, @PathVariable("reviewId") Long reviewId){
		//Authentication으로 사용자 받아와야 함.
		reviewService.deleteReview(videoId, reviewId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
