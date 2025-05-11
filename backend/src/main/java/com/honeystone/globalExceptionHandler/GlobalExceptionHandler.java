package com.honeystone.globalExceptionHandler;

import com.honeystone.common.dto.ApiError;
import com.honeystone.exception.video.FileStorageException;
import com.honeystone.exception.video.VideoCreationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(VideoCreationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleVideoCreation(VideoCreationException ex) {
        return new ApiError("비디오 생성에 실패했습니다.", ex.getMessage());
    }

    @ExceptionHandler(FileStorageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleFileStorage(FileStorageException ex) {
        return new ApiError("파일 저장에 실패했습니다.", ex.getMessage());
    }

    // 기타 DataAccessException 등도 필요하다면…
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleDB(DataAccessException ex) {
        return new ApiError("데이터베이스 처리 중 오류가 발생했습니다.", ex.getMessage());
    }
}
