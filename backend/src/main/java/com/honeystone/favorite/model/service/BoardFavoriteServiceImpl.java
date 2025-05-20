package com.honeystone.favorite.model.service;

import java.util.List;

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
	public void addFavorite(String userEmail, Long userId, Long boardId) throws ServerException {
		//사용자 유효성 체크
		if(userDao.findByEmail(userEmail) == null) throw new BusinessException("존재하지 않는 사용자입니다.");
		
		//게시글 유효성 체크
		if (boardId == null || boardDao.existsById(boardId) == 0) {
			throw new BusinessException("존재하지 않는 게시물입니다.");
		}
		
		boardFavoriteDao.insertFavorite(userId, boardId);
	}

	@Override
	public void removeFavorite(String userEmail, Long userId, Long boardId) throws ServerException {
		if(userDao.findByEmail(userEmail) == null) throw new BusinessException("존재하지 않는 사용자입니다.");
		boardFavoriteDao.deleteFavorite(userId, boardId);
	}

}
