package com.honeystone.board.model.service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.honeystone.common.security.MyUserPrincipal;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.honeystone.board.model.dao.BoardDao;
import com.honeystone.common.dto.board.Board;
import com.honeystone.common.dto.board.BoardFile;
import com.honeystone.common.dto.board.GetBoard;
import com.honeystone.common.dto.searchCondition.SearchBoardCondition;
import com.honeystone.common.dto.theClimb.TheClimb;
import com.honeystone.common.util.FileRemove;
import com.honeystone.common.util.FileUpload;
import com.honeystone.exception.BusinessException;
import com.honeystone.exception.ServerException;
import com.honeystone.user.model.dao.UserDao;

@Transactional
@Service
public class BoardServiceImpl implements BoardService {

	private final BoardDao boardDao;
	private final FileUpload fileUpload;
	private final FileRemove fileRemove;
	private final UserDao userDao;

	public BoardServiceImpl(BoardDao boardDao, FileUpload fileUpload, FileRemove fileRemove, UserDao userDao) {
		this.boardDao = boardDao;
		this.fileUpload = fileUpload;
		this.fileRemove = fileRemove;
		this.userDao = userDao;
	}

	@Override
	public Page<GetBoard> getBoardList( MyUserPrincipal user, SearchBoardCondition search, Pageable pageable) throws ServerException {

		Long userId = -1L;
		if(user != null){
			userId = user.getId();
		}
		// 페이지네이션
		long total = boardDao.countBoards(search);
		List<GetBoard> boards = boardDao.getBoardList(userId, search, pageable);

		return new PageImpl<>(boards, pageable, total);
	}

	@Override
	public GetBoard getBoard(MyUserPrincipal user, Long id) throws ServerException {
		Long userId = -1L;
		if(user != null){
			userId = user.getId();
		}
		// 있는 게시물인지 확인
		if(boardDao.existsById(id) == 0) throw new BusinessException("존재하지 않는 게시물입니다.");
		GetBoard board = boardDao.getBoard(userId, id);
		return board;
	}



	@Override
	public void createBoard(Long userId, Board board, MultipartFile file) throws IOException {
		// 사용자 유효성 체크
		if(userDao.existsById(userId) == 0) throw new BusinessException("존재하지 않는 사용자입니다.");

		if(file.isEmpty() || file == null) throw new BusinessException("파일 첨부는 필수입니다.");
		// board 생성 로직
		Board newBoard = Board.builder()
				.title(board.getTitle())
				.description(board.getDescription())
				.level(board.getLevel())
				.skill(board.getSkill())
				.holdColor(board.getHoldColor())
				.userId(userId)
				.build();
		
		
		try {
			boardDao.createBoard(newBoard);
		} catch (DataAccessException e) {
			throw new ServerException("게시물 생성 중 DB 오류가 발생했습니다.", e);
		}
		
		// the climb - board 매핑 테이블에 저장 로직
		TheClimb theClimb = TheClimb.builder()
				.id(-1L)
				.location(board.getLocation())
				.wall(board.getWall())
				.build();


		
		Long theClimbId = boardDao.findTheClimb(theClimb);
		if(theClimbId == null) throw new BusinessException("해당 클라이밍 정보가 없습니다.");

		try {	
			boardDao.createTheClimbBoard(newBoard.getId(), theClimbId);
		}catch(DataAccessException e) {
			throw new ServerException("더클라임-게시판 매핑 중 DB 오류가 발생했습니다.");
		}

		// 파일 처리 로직
		Long fileId = null;
		String filename = fileUpload.generateFileName("board", newBoard.getId(), file);
		String fileUrl = null;
		try {
			fileUrl = fileUpload.uploadFile(file, filename, "boards");
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
	public void updateBoard(Long userId, Long id, Board board, MultipartFile file) throws ServerException {
		// 사용자 유효성 체크
		if(userDao.existsById(userId) == 0) throw new BusinessException("존재하지 않는 사용자입니다.");

		// 있는 게시물인지 확인
		GetBoard checkBoard = boardDao.getBoard(userId, id);
		if(checkBoard == null) throw new BusinessException("존재하지 않는 게시물입니다.");
		if(checkBoard.getUserId() != userId) throw new BusinessException("해당 게시물을 수정할 권한이 없습니다.");

		// 클라이밍 정보 처리 (location, wall이 변경된 경우)
		if(board.getLocation() != null && board.getWall() != null) {
			TheClimb theClimb = TheClimb.builder()
				.id(-1L)
				.location(board.getLocation())
				.wall(board.getWall())
				.build();
			Long theClimbId = boardDao.findTheClimb(theClimb);
			if(theClimbId == null) throw new BusinessException("해당 클라이밍 정보가 없습니다.");

			try {
				boardDao.updateTheClimbBoard(id, theClimbId);
			} catch(DataAccessException e) {
				throw new ServerException("장소보드 매핑 중 DB 오류가 발생했습니다.");
			}
		}

		// 파일 처리
		if(file != null && !file.isEmpty()) {
			// 새 파일이 업로드된 경우

			// 1. 기존 파일 삭제
			try {
				BoardFile existingFile = boardDao.getBoardFile(id);
				if(existingFile != null) {
					// S3에서 기존 파일 삭제
					fileRemove.removeFile(existingFile.getFileId());
					// DB에서 기존 파일 정보 삭제
					boardDao.deleteFileById(existingFile.getFileId());
				}
			} catch(Exception e) {
				// 기존 파일 삭제 실패해도 계속 진행
				System.err.println("기존 파일 삭제 실패: " + e.getMessage());
			}

			// 2. 새 파일 업로드
			String filename = fileUpload.generateFileName("board", id, file);
			String fileUrl = null;
			try {
				fileUrl = fileUpload.uploadFile(file, filename, "boards");
			} catch (IOException e) {
				throw new ServerException("S3 파일 업로드 중 오류가 발생했습니다.", e);
			}

			// 3. 새 파일 정보 DB 저장
			BoardFile newFile = BoardFile.builder()
				.boardId(id)
				.filename(filename)
				.url(fileUrl)
				.build();

			try {
				boardDao.createFile(newFile);
			} catch (DataAccessException e) {
				// 파일 정보 저장 실패 시 S3에서 파일 삭제
				if (fileUrl != null) {
					fileRemove.removeFile(newFile.getFileId());
				}
				throw new ServerException("파일 정보 저장 중 DB 오류가 발생했습니다.", e);
			}
		}
		// file이 null이거나 empty면 기존 파일 유지

		// 게시글 정보 수정
		Board updateBoard = Board.builder()
			.id(id)
			.title(board.getTitle())
			.description(board.getDescription())
			.level(board.getLevel())
			.skill(board.getSkill())
			.holdColor(board.getHoldColor())
			.build();

		try {
			boardDao.updateBoard(updateBoard);
		} catch (DataAccessException e) {
			throw new ServerException("게시글 수정 중 DB 오류가 발생했습니다.", e);
		}
	}

	@Override
	public void deleteBoard(MyUserPrincipal user, Long id) throws ServerException {
		Long userId = -1L;
		if(user != null){
			userId = user.getId();
		}

		// 사용자 유효성 체크
		if(userDao.existsById(userId) == 0) throw new BusinessException("존재하지 않는 사용자입니다.");

		// 있는 게시물인지 확인
		GetBoard checkBoard = boardDao.getBoard(userId, id);
		if(checkBoard == null) throw new BusinessException("존재하지 않는 게시물입니다.");
		if(checkBoard.getUserId() != userId) throw new BusinessException(("해당 게시물을 삭제할 권한이 없습니다."));

		// 삭제
		// 1. db에서 변경
		GetBoard deletedBoard = getBoard(user, id);
		boardDao.deleteBoard(id); //deletedat 변경
		// file s3에서 경로 옮기기
		fileRemove.moveFile(deletedBoard.getUrl(), deletedBoard.getFilename());
	}

}
