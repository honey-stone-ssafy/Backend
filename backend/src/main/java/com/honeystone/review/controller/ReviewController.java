package com.honeystone.review.controller;

import java.util.List;

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

import com.honeystone.common.dto.GetReview;
import com.honeystone.common.dto.Review;
import com.honeystone.review.model.service.ReviewService;

@RestController
@RequestMapping("/api/videos/{videoId}/reviews")
public class ReviewController {

	private final ReviewService reviewService;
	
	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}
	
	// todo: 스웨거 작성 
	@GetMapping()
	public ResponseEntity<List<GetReview>> getReviewList(@PathVariable("videoId") Long videoId){
		//Authentication으로 사용자 받아와야 함.
		List<GetReview> reviews = reviewService.getReviewList(videoId);
		
		return new ResponseEntity<List<GetReview>>(reviews, HttpStatus.OK);
	}
	
	// todo: 스웨거 작성
	@PostMapping()
	public ResponseEntity<Void> createReview(@PathVariable("videoId") Long videoId, @RequestBody Review review){
		//Authentication으로 사용자 받아와야 함.
		reviewService.createReview(videoId, review);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	// todo: 스웨거 작성
	@PatchMapping("/{reviewId}")
	public ResponseEntity<Void> updateReview(@PathVariable("videoId") Long videoId, @PathVariable("reviewId") Long reviewId, @RequestBody Review review){
		//Authentication으로 사용자 받아와야 함.
		reviewService.updateReview(videoId, reviewId, review);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	// todo: 스웨거 작성
	@DeleteMapping("/{reviewId}")
	public ResponseEntity<Void> deleteReview(@PathVariable("videoId") Long videoId, @PathVariable("reviewId") Long reviewId){
		//Authentication으로 사용자 받아와야 함.
		reviewService.deleteReview(videoId, reviewId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
