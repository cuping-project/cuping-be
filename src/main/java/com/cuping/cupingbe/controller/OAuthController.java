package com.cuping.cupingbe.controller;

import com.cuping.cupingbe.global.util.Message;
import org.springframework.http.ResponseEntity;
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

	@GetMapping("/users/oauth/kakao")
	public ResponseEntity<Message> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
		return kakaoService.kakaoLogin(code, response);
	}
}
