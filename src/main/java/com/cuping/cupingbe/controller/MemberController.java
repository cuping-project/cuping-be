package com.cuping.cupingbe.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.cuping.cupingbe.dto.MemberLoginRequestDto;
import com.cuping.cupingbe.dto.MemberSignupRequestDto;
import com.cuping.cupingbe.global.security.UserDetailsImpl;
import com.cuping.cupingbe.service.MemberService;
import com.cuping.cupingbe.global.util.Message;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/signup/{type}")
	public ResponseEntity<Message> signup(@PathVariable String type, @Valid @RequestBody MemberSignupRequestDto memberSignupRequestDto) throws Exception {
		return memberService.signup(type, memberSignupRequestDto);
	}

	@PostMapping( "/signup/owner")
	public ResponseEntity<Message> ownerSignup(@Valid @ModelAttribute MemberSignupRequestDto memberSignupRequestDto) throws Exception {
		return memberService.signup("owner", memberSignupRequestDto);
	}

	@PostMapping("/checkId")
	public ResponseEntity<Message> checkId(@RequestBody Map<String, String> userId) {
		return memberService.duplicateCheckId(userId);
	}
	@PostMapping("/checkNickname")
	public ResponseEntity<Message> checkNickname(@RequestBody Map<String, String> nickname) {
		return memberService.duplicateCheckNickname(nickname);
	}
	@PostMapping("/login")
	public ResponseEntity<Message> login(@RequestBody MemberLoginRequestDto memberLoginRequestDto, HttpServletResponse response) {
		return memberService.login(memberLoginRequestDto, response);
	}

	@PostMapping("/logout")
	public ResponseEntity<Message>logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return memberService.logout(userDetails.getUser());
	}
}
