package com.honeystone.plan.model.dao;

import com.honeystone.common.dto.plan.Plan;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PlanDao {
   public List<Plan> getPlanList(Long userId);

    public Plan getPlan(Long id);

    public int existsPlanById(Long id);

    public void createPlan(Plan plan);

    public void updatePlan(Plan plan);

    public void deletePlan(Long id);

    public List<Plan> findPlansToDelete();

    public void completeDeletePlan(Long id);
}
