package com.honeystone.review.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.honeystone.common.dto.Review;
import com.honeystone.review.model.dao.ReviewDao;

@Service
public class ReviewServiceImpl implements ReviewService {

	private final ReviewDao reviewDao;

	public ReviewServiceImpl(ReviewDao reviewDao) {
		this.reviewDao = reviewDao;
	}

	@Override
	public List<Review> getReviewList(Long videoId) { // 전역 예외 처리
		// todo: 사용자 인증

		// video 존재 여부 검사

		List<Review> reviews = reviewDao.getReviewList(videoId);
		return reviews;
	}

	@Override
	public void createReview(Long videoId, Review review) { // 전역 예외 처리
		// todo: 사용자 인증

		// todo: videoId로 비디오 찾기 없으면 예외 처리

		Review newReview = Review.builder().content(review.getContent()).videoId(videoId).userId(1L) // 사용자 받으면 사용자 id
																										// 넣기.
				.build();
		reviewDao.createReview(newReview);
	}

	@Override
	public void updateReview(Long videoId, Long reviewId, Review review) { // 전역 예외 처리
		// todo: 사용자 인증
		// todo: videoId로 비디오 찾기 없으면 예외 처리

		if (reviewDao.existsById(reviewId) == 0)
			throw new RuntimeException("없는 댓글입니다."); // pull 받으면 커스텀 예외로 수정하기

		Review updatedReview = Review.builder().id(reviewId).content(review.getContent()).build();

		reviewDao.updatedReview(updatedReview);

	}

	@Override
	public void deleteReview(Long videoId, Long reviewId) {
		// todo: 사용자 인증
		// todo: videoId로 비디오 찾기 없으면 예외 처리

		if (reviewDao.existsById(reviewId) == 0)
			throw new RuntimeException("없는 댓글입니다."); // pull 받으면 커스텀 예외로 수정하기

		reviewDao.deleteReview(reviewId);
	}

}
