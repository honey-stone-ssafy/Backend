package com.honeystone.board.model.service;

import java.io.IOException;
import java.util.List;

import com.honeystone.common.dto.board.GetBoard;
import com.honeystone.common.dto.board.Board;
import org.springframework.web.multipart.MultipartFile;

public interface BoardService {
	public List<Board> getBoardList();

	public void createBoard(Board board, MultipartFile file) throws IOException;

	public void updateBoard(Long id, Board board);

	public void deleteBoard(Long id);

	public GetBoard getBoard(Long id);
}
