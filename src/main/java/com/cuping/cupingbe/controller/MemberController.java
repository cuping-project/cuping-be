package com.cuping.cupingbe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.cuping.cupingbe.dto.MemberLoginRequestDto;
import com.cuping.cupingbe.dto.MemberSignupRequest;
import com.cuping.cupingbe.dto.MemberResponseDto;
import com.cuping.cupingbe.global.security.UserDetailsImpl;
import com.cuping.cupingbe.service.MemberService;
import com.cuping.cupingbe.global.util.Message;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/signup/{type}")
	public ResponseEntity<Message> signup(@PathVariable String type,  @RequestBody MemberSignupRequest memberSignupRequest){
		return memberService.signup(type, memberSignupRequest);
	}

	@PostMapping("/login")
	public ResponseEntity<Message> login(@RequestBody MemberLoginRequestDto memberLoginRequestDto, HttpServletResponse response) {
		return memberService.login(memberLoginRequestDto,response);

	}

	@PostMapping("/logout")
	public ResponseEntity<Message>logout(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
		return memberService.logout(userDetails.getUser(), response);
	}
}
