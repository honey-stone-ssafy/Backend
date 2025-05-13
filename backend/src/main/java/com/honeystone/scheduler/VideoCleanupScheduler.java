package com.honeystone.scheduler;

import com.honeystone.common.dto.review.Review;
import com.honeystone.common.dto.video.Video;
import com.honeystone.common.util.FileRemove;
import com.honeystone.review.model.dao.ReviewDao;
import com.honeystone.video.model.dao.VideoDao;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class VideoCleanupScheduler {

    private final VideoDao videoDao;
    private final FileRemove fileRemove;
    private final ReviewDao reviewDao;

    public VideoCleanupScheduler(VideoDao videoDao, FileRemove fileRemove, ReviewDao reviewDao) {
        this.videoDao = videoDao;
        this.fileRemove = fileRemove;
        this.reviewDao = reviewDao;
    }

    @Scheduled(cron = "0 0 2 * * *") // 매일 새벽 2시에 실행
//    @Scheduled(cron = "0 */1 * * * *") // 매 1분마다 실행 (테스트용)
    public void deleteExpiredVideos() {
        // 게시물 검사
        List<Video> expiredVideos = videoDao.findVideosToDelete();
        System.out.println(expiredVideos);
        for (Video video : expiredVideos) {
            Long id = video.getId();
            videoDao.deleteFile(id);
            videoDao.completeDeleteVideo(id);
            fileRemove.removeFile(id);
        }

        // 댓글 검사
        List<Review> expiredReviews = reviewDao.findReviewsToDelete();
        System.out.println(expiredReviews);
        for(Review review : expiredReviews){
            Long id = review.getId();
            reviewDao.completeDeleteReview(id);
        }
    }
}
