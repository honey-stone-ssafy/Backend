package com.honeystone.recommandation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.honeystone.board.model.type.Level;
import com.honeystone.recommandation.model.service.RecommandService;
import com.honeystone.recommandation.model.type.LocClass;


import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/recommandations")
@Tag(name= "Recommand API", description = "추천 관련 API 입니다.")
public class RecommandController {
	
	private final RecommandService recommandService;
	
	public RecommandController(RecommandService recommandService) {
		this.recommandService = recommandService;
	}
	
	@GetMapping("")
	public ResponseEntity<List<LocClass>> getRecommandationList(@RequestParam Level level){
		List<LocClass> list = recommandService.getRecommandationList(level);

		return new ResponseEntity<List<LocClass>>(list, HttpStatus.OK);
	}
}
