package com.honeystone.board.model.service;

import java.io.IOException;

import com.honeystone.common.security.MyUserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.honeystone.common.dto.board.Board;
import com.honeystone.common.dto.board.GetBoard;
import com.honeystone.common.dto.searchCondition.SearchBoardCondition;
import com.honeystone.common.dto.theClimb.TheClimb;

public interface BoardService {
	public Page<GetBoard> getBoardList(MyUserPrincipal user, SearchBoardCondition search, Pageable pageable);

	public void createBoard(Long userId, Board board, MultipartFile file) throws IOException;

	public void updateBoard(Long userId, Long id, Board board, MultipartFile file);

	public void deleteBoard(MyUserPrincipal user, Long id);

	public GetBoard getBoard(MyUserPrincipal user, Long id);
}
