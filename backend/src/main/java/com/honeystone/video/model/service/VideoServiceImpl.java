package com.honeystone.video.model.service;

import java.io.IOException;
import java.util.List;

import com.honeystone.common.util.FileUpload;
import com.honeystone.common.dto.video.VideoFile;
import com.honeystone.exception.video.FileStorageException;
import com.honeystone.exception.video.VideoCreationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.honeystone.video.model.dao.VideoDao;
import com.honeystone.common.dto.video.Video;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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

		try{
			videoDao.createVideo(newVideo);
		} catch (DataAccessException dae) {
			throw new VideoCreationException("비디오 생성 중 DB 오류", dae);
		}
		// 파일 처리 로직 (비디오 먼저 생성하고, 그 id를 받아와야 함. file url에 저장하기 위해)
		// 1. file로 File 생성하기
		Long fileId = null;
		String filename = fileUpload.generateFileName(newVideo.getId(), file);
		String fileUrl = null;
		try {
			fileUrl = fileUpload.uploadFile(file, filename);
		}catch (IOException e) {
			throw new FileStorageException("S3업로드 중 I/O 오류", e);
		}

			VideoFile newFile = VideoFile.builder()
				.videoId(newVideo.getId())
				.filename(filename)
				.url(fileUrl)
				.build();

		try{
			// 2. DB에 저장
			videoDao.createFile(newFile);
			fileId = newFile.getFileId();
			System.out.println("******newFile : "+newFile);

		}catch (DataAccessException ex) {
			// S3 업로드 또는 file 테이블 저장 실패 시
			ex.printStackTrace();

			// S3에 올라간 파일이 있다면 삭제
			if (fileUrl != null) {
				fileUpload.deleteFile(fileId);
			}
			// 클라이언트에게도 실패 알리기
			throw new FileStorageException("파일 저장 중 DB 오류.", ex);
		}
	}
}
