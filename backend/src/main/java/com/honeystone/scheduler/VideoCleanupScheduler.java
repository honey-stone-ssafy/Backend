package com.honeystone.scheduler;

import com.honeystone.common.dto.video.Video;
import com.honeystone.common.util.FileRemove;
import com.honeystone.video.model.dao.VideoDao;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VideoCleanupScheduler {

    private final VideoDao videoDao;
    private final FileRemove fileRemove;

    public VideoCleanupScheduler(VideoDao videoDao, FileRemove fileRemove) {
        this.videoDao = videoDao;
        this.fileRemove = fileRemove;
    }

    @Scheduled(cron = "0 0 22 * * *") // 매일 새벽 2시에 실행
//    @Scheduled(cron = "0 */1 * * * *") // 매 1분마다 실행 (테스트용)
    public void deleteExpiredVideos() {
        List<Video> expiredVideos = videoDao.findVideosToDelete();
        System.out.println(expiredVideos);
        for (Video video : expiredVideos) {
            Long id = video.getId();
            videoDao.deleteFile(id);
            videoDao.completeDeleteVideo(id);
            fileRemove.removeFile(id);
        }
    }
}
