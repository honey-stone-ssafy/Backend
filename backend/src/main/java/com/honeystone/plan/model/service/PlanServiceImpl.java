package com.honeystone.plan.model.service;

import com.honeystone.common.dto.plan.Plan;
import com.honeystone.exception.BusinessException;
import com.honeystone.exception.ServerException;
import com.honeystone.plan.model.dao.PlanDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PlanServiceImpl implements PlanService {

    private final PlanDao planDao;

    public PlanServiceImpl(PlanDao planDao) {
        this.planDao = planDao;
    }

    @Override
    public List<Plan> getPlanList(Long userId) throws ServerException {
        // todo: 사용자 인증
        List<Plan> plans = planDao.getPlanList(userId);
        return plans;
    }

    @Override
    public Plan getPlan(Long userId, Long id) throws ServerException {
        // todo: 사용자 인증

        // 일정 유효성 체크
        if(planDao.existsPlanById(id) == 0) throw new BusinessException("존재하지 않는 일정입니다.");

        Plan plan = planDao.getPlan(id);
        return plan;
    }

    @Override
    public void createPlan(Long userId, Plan plan) throws ServerException {
        //todo: 사용자 인증

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
        //todo: 사용자 인증

        // 일정 유효성 체크
        if(planDao.existsPlanById(id) == 0) throw new BusinessException("존재하지 않는 일정입니다.");

        Plan updatedPlan = Plan.builder()
            .id(id)
            .title(plan.getTitle())
            .start(plan.getStart())
            .end(plan.getEnd())
            .scope(plan.getScope())
            .memo(plan.getMemo())
            .userId(userId)
            .location(plan.getLocation())
            .build();

        planDao.updatePlan(updatedPlan);
    }

    @Override
    public void deletePlan(Long uesrId, Long id) throws ServerException {
        //todo: 사용자 인증

        // 일정 유효성 체크
        if(planDao.existsPlanById(id) == 0) throw new BusinessException("존재하지 않는 일정입니다.");
        planDao.deletePlan(id);
    }
}
