package com.cuping.cupingbe.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cuping.cupingbe.service.KakaoService;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor public class OAuthController {

	private final KakaoService kakaoService;

	@GetMapping("/oauth/kakao")
	public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
		return kakaoService.kakaoLogin(code, response);
	}
}
