package com.honeystone.review.model.service;

import java.util.List;

import com.honeystone.common.dto.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

	public Page<Review> getReviewList(Long videoId, Pageable pageables);

	public void createReview(Long videoId, Review review);

	public void updateReview(Long videoId, Long reviewId, Review review);

	public void deleteReview(Long videoId, Long reviewId);

}
