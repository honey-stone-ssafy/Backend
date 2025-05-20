package com.honeystone.favorite.model.service;

import java.util.List;

import com.honeystone.common.security.MyUserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.honeystone.board.model.dao.BoardDao;
import com.honeystone.common.dto.board.GetBoard;
import com.honeystone.exception.BusinessException;
import com.honeystone.exception.ServerException;
import com.honeystone.favorite.model.dao.BoardFavoriteDao;
import com.honeystone.user.model.dao.UserDao;

@Transactional
@Service
public class BoardFavoriteServiceImpl implements BoardFavoriteService {
	
	private final BoardFavoriteDao boardFavoriteDao;
	private final UserDao userDao;
	private final BoardDao boardDao;
	
	public BoardFavoriteServiceImpl(BoardFavoriteDao boardFavoriteDao, UserDao userDao, BoardDao boardDao) {
		this.boardFavoriteDao = boardFavoriteDao;
		this.userDao = userDao;
		this.boardDao = boardDao;
	}

	@Override
	public Page<GetBoard> getFavoriteBoardList(Long userId, Pageable pageable) {
		long total = boardFavoriteDao.countBoards(userId);
		int offset = pageable.getPageNumber() * pageable.getPageSize();
	    int size = pageable.getPageSize();
	    
		List<GetBoard> boards = boardFavoriteDao.getBoardList(userId, offset, size);
		return new PageImpl<>(boards, pageable, total);
	}

	@Override
	public void addFavorite(MyUserPrincipal user, Long userId, Long boardId) throws ServerException {
		//사용자 유효성 체크
		if(userDao.findByEmail(user.getEmail()) == null) throw new BusinessException("존재하지 않는 사용자입니다.");
		if(user.getId() != userId) throw new BusinessException("좋아요를 추가할 권한이 없는 사용자입니다.");
		
		//게시글 유효성 체크
		if (boardId == null || boardDao.existsById(boardId) == 0) {
			throw new BusinessException("존재하지 않는 게시물입니다.");
		}

		//이미 좋아요 되어 있는 게시물 예외 처리
		
		boardFavoriteDao.insertFavorite(userId, boardId);
	}

	@Override
	public void removeFavorite(MyUserPrincipal user, Long userId, Long boardId) throws ServerException {
		if(userDao.findByEmail(user.getEmail()) == null) throw new BusinessException("존재하지 않는 사용자입니다.");
		if(user.getId() != userId) throw new BusinessException("좋아요를 삭제할 권한이 없는 사용자입니다.");

		if (boardId == null || boardDao.existsById(boardId) == 0) {
			throw new BusinessException("존재하지 않는 게시물입니다.");
		}

		//애초에 좋아요를 누르지 않은 게시물 예외 처리

		boardFavoriteDao.deleteFavorite(userId, boardId);
	}

}
