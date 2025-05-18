package com.honeystone.review.model.service;

import java.util.List;

import com.honeystone.common.dto.user.User;
import com.honeystone.user.model.dao.UserDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.honeystone.common.dto.review.Review;
import com.honeystone.exception.BusinessException;
import com.honeystone.exception.ServerException;
import com.honeystone.review.model.dao.ReviewDao;
import com.honeystone.board.model.dao.BoardDao;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

	private final ReviewDao reviewDao;
	private final BoardDao boardDao;
	private final UserDao userDao;

	public ReviewServiceImpl(ReviewDao reviewDao, BoardDao boardDao, UserDao userDao) {
		this.reviewDao = reviewDao;
		this.boardDao = boardDao;
		this.userDao = userDao;
	}

	@Override
	public Page<Review> getReviewList(Long boardId, Pageable pageable) throws ServerException {
		// 게시물 유효성 검사
		if (boardDao.existsById(boardId) == 0)
			throw new BusinessException("존재하지 않는 게시물입니다.");

		// 페이지네이션
		long total = reviewDao.countReviews(boardId);

		List<Review> reviews = reviewDao.getReviewList(boardId, pageable);
		return new PageImpl<>(reviews, pageable, total);
	}

	@Override
	public void createReview(String email, Long boardId, Review review) throws ServerException {
		// 사용자 유효성 체크
		User user = userDao.findByEmail(email);
		if(user == null) throw new BusinessException("존재하지 않는 사용자입니다.");

		if (boardDao.existsById(boardId) == 0)
			throw new BusinessException("존재하지 않는 게시물입니다.");

		Review newReview = Review.builder().content(review.getContent()).boardId(boardId).userId(user.getId())
			.build();
		reviewDao.createReview(newReview);
	}

	@Override
	public void updateReview(String email, Long boardId, Long reviewId, Review review) throws ServerException {
		// 사용자 유효성 체크
		User user = userDao.findByEmail(email);
		Review checkReview = reviewDao.existsById(reviewId);

		if(user == null) throw new BusinessException("존재하지 않는 사용자입니다.");
		else if (user.getId() != checkReview.getUserId()) throw new BusinessException("댓글을 수정할 권한이 없습니다.");

		if (boardDao.existsById(boardId) == 0)
			throw new BusinessException("존재하지 않는 게시물입니다.");

		if (checkReview == null){
			throw new BusinessException("존재하지 않는 댓글입니다."); // pull 받으면 커스텀 예외로 수정하기
		}else if(checkReview.getBoardId() != boardId){
			throw new BusinessException("해당 게시물에 존재하지 않는 댓글입니다.");
		}

		Review updatedReview = Review.builder().id(reviewId).content(review.getContent()).build();

		reviewDao.updatedReview(updatedReview);

	}

	@Override
	public void deleteReview(String email, Long boardId, Long reviewId) throws ServerException {
		// 사용자 유효성 체크
		User user = userDao.findByEmail(email);
		Review checkReview = reviewDao.existsById(reviewId);

		if(user == null) throw new BusinessException("존재하지 않는 사용자입니다.");
		else if (user.getId() != checkReview.getUserId()) throw new BusinessException("댓글을 수정할 권한이 없습니다.");

		if (boardDao.existsById(boardId) == 0)
			throw new BusinessException("존재하지 않는 게시물입니다.");

		if (checkReview == null){
			throw new BusinessException("존재하지 않는 댓글입니다.");
		}else if(checkReview.getBoardId() != boardId){
			throw new BusinessException("해당 게시물에 존재하지 않는 댓글입니다.");
		}

		reviewDao.deleteReview(reviewId);
	}

}