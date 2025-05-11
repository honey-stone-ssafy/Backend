package com.honeystone.video.model.service;

import java.io.IOException;
import java.util.List;

import com.honeystone.common.util.FileUpload;
import com.honeystone.common.dto.video.VideoFile;
import com.honeystone.exception.BusinessException;
import com.honeystone.exception.ServerException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.honeystone.video.model.dao.VideoDao;
import com.honeystone.common.dto.video.Video;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@Service
public class VideoServiceImpl implements VideoService {

	private final VideoDao videoDao;
	private final FileUpload fileUpload;
	
	public VideoServiceImpl(VideoDao videoDao, FileUpload fileUpload) {
		this.videoDao = videoDao;
		this.fileUpload = fileUpload;
	}
	
	@Override
	public List<Video> getVideoList() {
		System.out.println("게시글 전체 목록");
		return videoDao.selectAll();
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
			System.out.println("******newFile : "+newFile);

		} catch (DataAccessException e) {
			// S3 업로드 또는 file 테이블 저장 실패 시
			// S3에 올라간 파일이 있다면 삭제
			if (fileUrl != null) {
				fileUpload.deleteFile(fileId);
			}
			throw new ServerException("파일 정보 저장 중 DB 오류가 발생했습니다.", e);
		}
	}

	@Override
	public void updateVideo(Long id, Video video) throws ServerException {
		// 사용자 인증
		// 있는 게시물인지 확인
		if(videoDao.existsById(id) == 0) throw new BusinessException("없는 게시물입니다.");
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
}
