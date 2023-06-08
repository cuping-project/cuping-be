package com.cuping.cupingbe.service;

import com.cuping.cupingbe.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cuping.cupingbe.entity.User;
import com.cuping.cupingbe.entity.UserRoleEnum;
import com.cuping.cupingbe.global.exception.CustomException;
import com.cuping.cupingbe.global.exception.ErrorCode;
import com.cuping.cupingbe.global.jwt.JwtUtil;
import com.cuping.cupingbe.global.redis.util.RedisUtil;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final RedisUtil redisUtil;
	private final UtilService utilService;
	private final OwnerPageService ownerPageService;

	// 회원가입
	@Transactional
	public ResponseEntity<Message> signup(String type, MemberSignupRequestDto requestDto) throws Exception {
		UserRoleEnum role = checkType(type, requestDto.getAdminKey());
		duplicateCheckId(Map.of("userId", requestDto.getUserId()));
		duplicateCheckNickname(Map.of("nickname", requestDto.getNickname()));
		User user = userRepository.save(new User(
				requestDto.getUserId(),
				passwordEncoder.encode(requestDto.getPassword()),
				requestDto.getNickname(), role)
		);
		if (type.equals("owner")) {
			ownerPageService.createCafe(new OwnerPageRequestDto(
					requestDto.getStoreName()
					, requestDto.getStoreAddress()
					, requestDto.getStoreNumber()
					, requestDto.getAuthImage())
					, user
			);
		}
		return new ResponseEntity<>(new Message("회원가입 성공", null), HttpStatus.NO_CONTENT);
	}

	public UserRoleEnum checkType(String type, String adminKey) {
		UserRoleEnum role;
		switch (type) {
			case "user" -> role = UserRoleEnum.USER;
			case "admin" -> {
				role = UserRoleEnum.ADMIN;
				if (!jwtUtil.checkAdminKey(adminKey))
					throw new CustomException(ErrorCode.INVALID_ADMIN_KEY);
			}
			case "owner" -> role = UserRoleEnum.OWNER;
			default -> throw new CustomException(ErrorCode.INVALID_TYPE);
		}
		return role;
	}

	public ResponseEntity<Message> duplicateCheckId(Map<String, String> userId) {
		if (userRepository.findByUserId(userId.get("userId")).isPresent())
			throw new CustomException(ErrorCode.DUPLICATE_IDENTIFIER);
		return new ResponseEntity<>(new Message("사용 가능한 아이디입니다.", null), HttpStatus.NO_CONTENT);
	}

	public ResponseEntity<Message> duplicateCheckNickname(Map<String, String> nickname) {
		if (userRepository.findByNickname(nickname.get("nickname")).isPresent())
			throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
		return new ResponseEntity<>(new Message("사용 가능한 닉네임입니다.", null), HttpStatus.NO_CONTENT);
	}

	// 로그인
	public ResponseEntity<Message> login(MemberLoginRequestDto memberLoginRequestDto, HttpServletResponse response){
		User user = utilService.checkUserId(memberLoginRequestDto.getUserId());
		utilService.checkUserPassword(user.getUserId(), memberLoginRequestDto.getPassword());
		createLoginToken(user.getUserId(), user.getRole(), response);
		return new ResponseEntity<>(new Message("로그인 성공", null), HttpStatus.NO_CONTENT);
	}

	public void createLoginToken(String userId, UserRoleEnum role, HttpServletResponse response) {
		TokenDto tokenDto = jwtUtil.creatAllToken(userId, role);
		redisUtil.set(userId, tokenDto.getRefreshToken(), JwtUtil.REFRESH_TIME);
		jwtUtil.setCookies(response, tokenDto);
	}

	// 로그아웃
	public ResponseEntity<Message> logout(User user){
		setLogoutBlackList(user.getUserId());
		return new ResponseEntity<>(new Message("로그아웃 성공", null), HttpStatus.NO_CONTENT);
	}

	public void setLogoutBlackList(String userId) {
		String refreshToken;
		if (redisUtil.get(userId).isPresent())
			refreshToken = redisUtil.get(userId).get().toString().substring(7);
		else
			return ;
		Long expireTime = jwtUtil.getExpirationTime(refreshToken);
		redisUtil.delete(userId);
		redisUtil.setBlackList(userId, refreshToken, expireTime);
	}
}