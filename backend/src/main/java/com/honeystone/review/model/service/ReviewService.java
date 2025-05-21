package com.honeystone.review.model.service;

import java.util.List;

import com.honeystone.common.dto.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

	public Page<Review> getReviewList(Long boardId, Pageable pageables);

	public void createReview(String email, Long boardId, Review review);

	public void updateReview(String email, Long boardId, Long reviewId, Review review);

	public void deleteReview(String email, Long boardId, Long reviewId);

}
