package com.honeystone.scheduler;

import com.honeystone.common.dto.plan.Plan;
import com.honeystone.common.dto.board.Board;
import com.honeystone.common.dto.review.Review;
import com.honeystone.common.util.FileRemove;
import com.honeystone.plan.model.dao.PlanDao;
import com.honeystone.board.model.dao.BoardDao;
import com.honeystone.review.model.dao.ReviewDao;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class CleanupScheduler {

    private final BoardDao boardDao;
    private final FileRemove fileRemove;
    private final PlanDao planDao;
    private final ReviewDao reviewDao;

    public CleanupScheduler(BoardDao boardDao, FileRemove fileRemove, PlanDao planDao, ReviewDao reviewDao) {
        this.boardDao = boardDao;
        this.fileRemove = fileRemove;
        this.planDao = planDao;
        this.reviewDao = reviewDao;
    }

    @Scheduled(cron = "0 0 2 * * *") // 매일 새벽 2시에 실행
//    @Scheduled(cron = "0 */1 * * * *") // 매 1분마다 실행 (테스트용)
    public void deleteExpiredBoards() {
        List<Board> expiredBoards = boardDao.findBoardsToDelete();
        System.out.println(expiredBoards);
        for (Board board : expiredBoards) {
            Long id = board.getId();
            boardDao.deleteFile(id);
            boardDao.completeDeleteBoard(id);
            fileRemove.removeFile(id);
        }
    }

    // 일정 영구 삭제
    @Scheduled(cron = "0 0 2 * * *") // 매일 새벽 2시에 실행
//    @Scheduled(cron = "0 */1 * * * *") // 매 1분마다 실행 (테스트용)
    public void deleteExpiredPlans(){
        List<Plan> expiredPlans = planDao.findPlansToDelete();
        System.out.println(expiredPlans);

        for(Plan plan : expiredPlans){
            Long id = plan.getId();
            planDao.completeDeletePlan(id);
        }
    }

    @Scheduled(cron = "0 0 2 * * *") // 매일 새벽 2시에 실행
//    @Scheduled(cron = "0 */1 * * * *") // 매 1분마다 실행 (테스트용)
    public void deleteExpiredReviews(){
        // 댓글 검사
        List<Review> expiredReviews = reviewDao.findReviewsToDelete();
        System.out.println(expiredReviews);
        for(Review review : expiredReviews){
            Long id = review.getId();
            reviewDao.completeDeleteReview(id);
        }

    }
}
