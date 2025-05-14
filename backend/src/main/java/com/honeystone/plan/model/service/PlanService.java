package com.honeystone.plan.model.service;

import com.honeystone.common.dto.plan.Plan;

import java.util.List;

public interface PlanService {
    public List<Plan> getPlanList(Long userId);

    public Plan getPlan(Long userId, Long id);

    public void createPlan(Long userId, Plan plan);

    public void updatePlan(Long userId, Long id, Plan plan);

    public void deletePlan(Long uesrId, Long id);
}
