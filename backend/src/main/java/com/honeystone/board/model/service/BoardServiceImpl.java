package com.honeystone.board.model.service;

import java.io.IOException;
import java.net.BindException;
import java.util.List;

import com.honeystone.common.dto.board.GetBoard;
import com.honeystone.common.dto.searchCondition.SearchBoardCondition;
import com.honeystone.common.util.FileRemove;
import com.honeystone.common.util.FileUpload;
import com.honeystone.common.dto.board.BoardFile;
import com.honeystone.exception.BusinessException;
import com.honeystone.exception.ServerException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.honeystone.board.model.dao.BoardDao;
import com.honeystone.common.dto.board.Board;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@Service
public class BoardServiceImpl implements BoardService {

	private final BoardDao boardDao;
	private final FileUpload fileUpload;
	private final FileRemove fileRemove;

	public BoardServiceImpl(BoardDao boardDao, FileUpload fileUpload, FileRemove fileRemove) {
		this.boardDao = boardDao;
		this.fileUpload = fileUpload;
		this.fileRemove = fileRemove;
	}

	@Override
	public Page<GetBoard> getBoardList(SearchBoardCondition search, Pageable pageable) throws ServerException {

		// 페이지네이션
		long total = boardDao.countBoards(search);
		List<GetBoard> boards = boardDao.getBoardList(search, pageable);

		return new PageImpl<>(boards, pageable, total);
	}

	@Override
	public GetBoard getBoard(Long id) throws ServerException {
		// 있는 게시물인지 확인
		if(boardDao.existsById(id) == 0) throw new BusinessException("존재하지 않는 게시물입니다.");

		GetBoard board = boardDao.getBoard(id);
		return board;
	}



	@Override
	public void createBoard(Board board, MultipartFile file) throws IOException {
		// todo : 사용자 유효성 로직 필요

		if(file.isEmpty() || file == null) throw new BusinessException("파일 첨부는 필수입니다.");
		// board 생성 로직
		Board newBoard = Board.builder()
				.title(board.getTitle())
				.description(board.getDescription())
				.level(board.getLevel())
				.skill(board.getSkill())
				.build();

		try {
			boardDao.createBoard(newBoard);
		} catch (DataAccessException e) {
			throw new ServerException("비디오 생성 중 DB 오류가 발생했습니다.", e);
		}

		// 파일 처리 로직
		Long fileId = null;
		String filename = fileUpload.generateFileName(newBoard.getId(), file);
		String fileUrl = null;
		try {
			fileUrl = fileUpload.uploadFile(file, filename);
		} catch (IOException e) {
			throw new ServerException("S3 파일 업로드 중 오류가 발생했습니다.", e);
		}

		BoardFile newFile = BoardFile.builder()
			.boardId(newBoard.getId())
			.filename(filename)
			.url(fileUrl)
			.build();

		try {
			// DB에 저장
			boardDao.createFile(newFile);
			fileId = newFile.getFileId();
//			System.out.println("******newFile : "+newFile);

		} catch (DataAccessException e) {
			// S3 업로드 또는 file 테이블 저장 실패 시
			// S3에 올라간 파일이 있다면 삭제
			if (fileUrl != null) {
				fileRemove.removeFile(fileId);
			}
			throw new ServerException("파일 정보 저장 중 DB 오류가 발생했습니다.", e);
		}
	}

	@Override
	public void updateBoard(Long id, Board board) throws ServerException {
		// 사용자 인증
		// 있는 게시물인지 확인
		if(boardDao.existsById(id) == 0) throw new BusinessException("존재하지 않는 게시물입니다.");
		// 수정
		Board updateBoard = Board.builder()
			.id(id)
			.title(board.getTitle())
			.description(board.getDescription())
			.level(board.getLevel())
			.skill(board.getSkill())
			.build();
		boardDao.updateBoard(updateBoard);
	}

	@Override
	public void deleteBoard(Long id) throws ServerException {
		// todo: 사용자 인증

		// 있는 게시물인지 확인
		if(boardDao.existsById(id) == 0) throw new BusinessException("존재하지 않는 게시물입니다.");

		// 삭제
		// 1. db에서 변경
		GetBoard deletedBoard = getBoard(id);
		boardDao.deleteBoard(id); //deletedat 변경
		// file s3에서 경로 옮기기
		fileRemove.moveFile(deletedBoard.getUrl(), deletedBoard.getFilename());
	}

}
