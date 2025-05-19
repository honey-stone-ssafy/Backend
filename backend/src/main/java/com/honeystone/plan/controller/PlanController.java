package com.honeystone.plan.controller;

import com.honeystone.common.dto.plan.Plan;
import com.honeystone.common.security.MyUserPrincipal;
import com.honeystone.exception.ServerException;
import com.honeystone.plan.model.service.PlanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
@Tag(name= "Plan API", description = "일정 관련 API 입니다.")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    // todo: 스웨거 작성
    @GetMapping()
    public ResponseEntity<List<Plan>> getPlanList(@AuthenticationPrincipal MyUserPrincipal user){
        List<Plan> plans = planService.getPlanList(user.getId());
        return new ResponseEntity<List<Plan>>(plans, HttpStatus.OK);
    }

    // todo: 스웨거 작성
    @GetMapping("/{id}")
    public ResponseEntity<Plan> getPlan(@AuthenticationPrincipal MyUserPrincipal user, @PathVariable("id") Long id){

        Plan plan = planService.getPlan(user.getId(), id);
        return new ResponseEntity<>(plan, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Void> createPlan(@AuthenticationPrincipal MyUserPrincipal user, @Valid @RequestBody Plan plan) {

        planService.createPlan(user.getId(), plan);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePlan(@AuthenticationPrincipal MyUserPrincipal user, @PathVariable("id") Long id, @RequestBody Plan plan){

        System.out.println(plan);
        planService.updatePlan(user.getId(), id, plan);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@AuthenticationPrincipal MyUserPrincipal user, @PathVariable("id") Long id){

        planService.deletePlan(user.getId(), id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
