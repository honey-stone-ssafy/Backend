package com.honeystone.review.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.honeystone.common.dto.Review;

@Mapper
public interface ReviewDao {

	public List<Review> getReviewList(Long videoId);

	public void createReview(Review newReview);

	public int existsById(Long reviewId);

	public void updatedReview(Review updatedReview);

	public void deleteReview(Long reviewId);

	public void CompleteDeleteReview(Long reviewId);

}
