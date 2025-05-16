package com.honeystone.review.model.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.honeystone.common.dto.review.Review;
import com.honeystone.exception.BusinessException;
import com.honeystone.exception.ServerException;
import com.honeystone.review.model.dao.ReviewDao;
import com.honeystone.video.model.dao.VideoDao;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

	private final ReviewDao reviewDao;
	private final VideoDao videoDao;

	public ReviewServiceImpl(ReviewDao reviewDao, VideoDao videoDao) {
		this.reviewDao = reviewDao;
		this.videoDao = videoDao;
	}

	@Override
	public Page<Review> getReviewList(Long videoId, Pageable pageable) throws ServerException {
		// todo: 사용자 인증

		// 게시물 유효성 검사
		if (videoDao.existsById(videoId) == 0)
			throw new BusinessException("존재하지 않는 게시물입니다.");

		// 페이지네이션
		long total = reviewDao.countReviews(videoId);

		List<Review> reviews = reviewDao.getReviewList(videoId, pageable);
		return new PageImpl<>(reviews, pageable, total);
	}

	@Override
	public void createReview(Long videoId, Review review) throws ServerException {
		// todo: 사용자 인증

		if (videoDao.existsById(videoId) == 0)
			throw new BusinessException("존재하지 않는 게시물입니다.");

		Review newReview = Review.builder().content(review.getContent()).videoId(videoId).userId(1L) // 사용자 받으면 사용자 id 넣기.
			.build();
		reviewDao.createReview(newReview);
	}

	@Override
	public void updateReview(Long videoId, Long reviewId, Review review) throws ServerException {
		// todo: 사용자 인증

		if (videoDao.existsById(videoId) == 0)
			throw new BusinessException("존재하지 않는 게시물입니다.");

		Review checkReview = reviewDao.existsById(reviewId);
		if (checkReview == null){
			throw new BusinessException("존재하지 않는 댓글입니다."); // pull 받으면 커스텀 예외로 수정하기
		}else if(checkReview.getVideoId() != videoId){
			throw new BusinessException("해당 게시물에 존재하지 않는 댓글입니다.");
		}

		Review updatedReview = Review.builder().id(reviewId).content(review.getContent()).build();

		reviewDao.updatedReview(updatedReview);

	}

	@Override
	public void deleteReview(Long videoId, Long reviewId) throws ServerException {
		// todo: 사용자 인증
		if (videoDao.existsById(videoId) == 0)
			throw new BusinessException("존재하지 않는 게시물입니다.");

		if (reviewDao.existsById(reviewId) == null)
			throw new RuntimeException("존재하지 않는 댓글입니다."); // pull 받으면 커스텀 예외로 수정하기

		reviewDao.deleteReview(reviewId);
	}

}