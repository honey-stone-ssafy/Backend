package com.honeystone.video.model.service;

import java.io.IOException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.honeystone.common.dto.searchCondition.SearchBoardCondition;
import com.honeystone.common.dto.video.GetVideo;
import com.honeystone.common.dto.video.Video;
import com.honeystone.common.dto.video.VideoFile;
import com.honeystone.common.util.FileRemove;
import com.honeystone.common.util.FileUpload;
import com.honeystone.exception.BusinessException;
import com.honeystone.exception.ServerException;
import com.honeystone.video.model.dao.VideoDao;

@Transactional
@Service
public class VideoServiceImpl implements VideoService {

	private final VideoDao videoDao;
	private final FileUpload fileUpload;
	private final FileRemove fileRemove;

	public VideoServiceImpl(VideoDao videoDao, FileUpload fileUpload, FileRemove fileRemove) {
		this.videoDao = videoDao;
		this.fileUpload = fileUpload;
		this.fileRemove = fileRemove;
	}

	@Override
	public Page<GetVideo> getVideoList(SearchBoardCondition search, Pageable pageable) throws ServerException {
		
		// 페이지네이션
		long total = videoDao.countBoards(search);
		List<GetVideo> boards = videoDao.getVideoList(search, pageable);
		
		return new PageImpl<>(boards, pageable, total);
	}

	@Override
	public GetVideo getVideo(Long id) throws ServerException {
		// 있는 게시물인지 확인
		if(videoDao.existsById(id) == 0) throw new BusinessException("존재하지 않는 게시물입니다.");

		GetVideo video = videoDao.getVideo(id);
		return video;
	}

	@Override
	public void createVideo(Video video, MultipartFile file) throws IOException {
		// todo : 사용자 유효성 로직 필요

		// video 생성 로직
		Video newVideo = Video.builder()
				.title(video.getTitle())
				.description(video.getDescription())
				.level(video.getLevel())
				.skill(video.getSkill())
				.build();

		try {
			videoDao.createVideo(newVideo);
		} catch (DataAccessException e) {
			throw new ServerException("비디오 생성 중 DB 오류가 발생했습니다.", e);
		}

		// 파일 처리 로직
		Long fileId = null;
		String filename = fileUpload.generateFileName(newVideo.getId(), file);
		String fileUrl = null;
		try {
			fileUrl = fileUpload.uploadFile(file, filename);
		} catch (IOException e) {
			throw new ServerException("S3 파일 업로드 중 오류가 발생했습니다.", e);
		}

		VideoFile newFile = VideoFile.builder()
			.videoId(newVideo.getId())
			.filename(filename)
			.url(fileUrl)
			.build();

		try {
			// DB에 저장
			videoDao.createFile(newFile);
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
	public void updateVideo(Long id, Video video) throws ServerException {
		// 사용자 인증
		// 있는 게시물인지 확인
		if(videoDao.existsById(id) == 0) throw new BusinessException("존재하지 않는 게시물입니다.");
		// 수정
		Video updateVideo = Video.builder()
			.id(id)
			.title(video.getTitle())
			.description(video.getDescription())
			.level(video.getLevel())
			.skill(video.getSkill())
			.build();
		videoDao.updateVideo(updateVideo);
	}

	@Override
	public void deleteVideo(Long id) throws ServerException {
		// todo: 사용자 인증

		// 있는 게시물인지 확인
		if(videoDao.existsById(id) == 0) throw new BusinessException("존재하지 않는 게시물입니다.");

		// 삭제
		// 1. db에서 변경
		GetVideo deletedVideo = getVideo(id);
		videoDao.deleteVideo(id); //deletedat 변경
		// file s3에서 경로 옮기기
		fileRemove.moveFile(deletedVideo.getUrl(), deletedVideo.getFilename());
	}

}
