package com.honeystone.review.model.service;

import java.util.List;

import com.honeystone.common.dto.GetReview;
import com.honeystone.common.dto.Review;

public interface ReviewService {

	public List<GetReview> getReviewList(Long videoId);

	public void createReview(Long videoId, Review review);

	public void updateReview(Long videoId, Long reviewId, Review review);

	public void deleteReview(Long videoId, Long reviewId);

}
