package com.honeystone.review.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import com.honeystone.common.dto.review.Review;


@Mapper
public interface ReviewDao {

	public long countReviews(Long videoId);


	public List<Review> getReviewList(Long videoId);

	public List<Review> getReviewList(@Param("videoId") Long videoId,
									  @Param("pageable") Pageable pageable);



	public void createReview(Review newReview);

	public Review existsById(Long reviewId);

	public void updatedReview(Review updatedReview);

	public void deleteReview(Long reviewId);

	public void completeDeleteReview(Long reviewId);

	public List<Review> findReviewsToDelete();
}
