package com.honeystone.plan.controller;

import com.honeystone.common.dto.plan.Plan;
import com.honeystone.exception.ServerException;
import com.honeystone.plan.model.service.PlanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Plan>> getPlanList(){
        //todo: 사용자 인증
        Long userId = 1L; // 임시 값


        List<Plan> plans = planService.getPlanList(userId);
        return new ResponseEntity<List<Plan>>(plans, HttpStatus.OK);
    }

    // todo: 스웨거 작성
    @GetMapping("/{id}")
    public ResponseEntity<Plan> getPlan(@PathVariable("id") Long id){
        // todo: 사용자 인증
        Long userId = 1L; // 임시 값

        Plan plan = planService.getPlan(userId, id);
        return new ResponseEntity<>(plan, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Void> createPlan(@Valid @RequestBody Plan plan) {
        // todo: 사용자 인증
        Long userId = 1L; // 임시 값

        planService.createPlan(userId, plan);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePlan(@PathVariable("id") Long id, @RequestBody Plan plan){
        // todo: 사용자 인증
        Long userId = 1L;

        planService.updatePlan(userId, id, plan);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable("id") Long id){
        // todo: 사용자 인증
        Long uesrId = 1L;

        planService.deletePlan(uesrId, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
