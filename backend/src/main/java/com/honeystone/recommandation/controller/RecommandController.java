package com.honeystone.recommandation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/recommandations")
@Tag(name= "Recommand API", description = "추천 관련 API 입니다.")
public class RecommandController {

}
