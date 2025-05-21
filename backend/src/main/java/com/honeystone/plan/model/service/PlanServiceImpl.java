package com.honeystone.plan.model.service;

import com.honeystone.common.dto.plan.Plan;
import com.honeystone.common.dto.user.User;
import com.honeystone.exception.BusinessException;
import com.honeystone.exception.ServerException;
import com.honeystone.plan.model.dao.PlanDao;
import com.honeystone.user.model.dao.UserDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PlanServiceImpl implements PlanService {

    private final PlanDao planDao;
    private final UserDao userDao;

    public PlanServiceImpl(PlanDao planDao,  UserDao userDao) {
        this.planDao = planDao;
        this.userDao =  userDao;
    }

    @Override
    public List<Plan> getPlanList(Long userId) throws ServerException {
        // 사용자 유효성 체크
        if(userDao.existsById(userId) == 0) throw new BusinessException("존재하지 않는 사용자입니다.");

        List<Plan> plans = planDao.getPlanList(userId);
        return plans;
    }

    @Override
    public Plan getPlan(Long userId, Long id) throws ServerException {
        // 사용자 유효성 체크
        if(userDao.existsById(userId) == 0) throw new BusinessException("존재하지 않는 사용자입니다.");

        // 일정 유효성 체크
        if(planDao.existsPlanById(id) == 0) throw new BusinessException("존재하지 않는 일정입니다.");

        Plan plan = planDao.getPlan(id);

        return plan;
    }

    @Override
    public void createPlan(Long userId, Plan plan) throws ServerException {
        // 사용자 유효성 체크
        if(userDao.existsById(userId) == 0) throw new BusinessException("존재하지 않는 사용자입니다.");

        // 일단 빈칸이라고 생각하고 널값 대비해서 이렇게 작성했는데, 프론트에서 무조건 값을 주는 걸로 하면 그냥 plan.getEnd()로 해도 될 것 같아요!
        LocalDateTime end = plan.getEnd() == null ? plan.getStart().plusHours(1) : plan.getEnd();;

        Plan newPlan = Plan.builder()
            .title(plan.getTitle())
            .start(plan.getStart())
            .end(end)
            .scope(plan.getScope())
            .memo(plan.getMemo())
            .userId(userId)
            .location(plan.getLocation())
            .build();

        planDao.createPlan(newPlan);
    }

    @Override
    public void updatePlan(Long userId, Long id, Plan plan) throws ServerException {
        // 사용자 유효성 체크
        if(userDao.existsById(userId) == 0) throw new BusinessException("존재하지 않는 사용자입니다.");

        // 일정 유효성 체크
        Plan checkPlan = planDao.getPlan(id);
        if(checkPlan == null) throw new BusinessException("존재하지 않는 일정입니다.");
        if(userId != checkPlan.getUserId()) throw new BusinessException("해당 일정을 수정할 권한이 없습니다.");

        Plan updatedPlan = Plan.builder()
            .id(id)
            .title(plan.getTitle())
            .start(plan.getStart())
            .end(plan.getEnd())
            .scope(plan.getScope())
            .memo(plan.getMemo())
            .location(plan.getLocation())
            .userId(userId)
            .build();

        planDao.updatePlan(updatedPlan);
    }

    @Override
    public void deletePlan(Long userId, Long id) throws ServerException {
        // 사용자 유효성 체크
        if(userDao.existsById(userId) == 0) throw new BusinessException("존재하지 않는 사용자입니다.");
        // 일정 유효성 체크
        if(planDao.existsPlanById(id) == 0) throw new BusinessException("존재하지 않는 일정입니다.");

        Plan plan = planDao.getPlan(id);
        if(plan.getUserId() != userId) throw new BusinessException("해당 일정을 삭제할 권한이 없습니다.");
        planDao.deletePlan(id);
    }
}
