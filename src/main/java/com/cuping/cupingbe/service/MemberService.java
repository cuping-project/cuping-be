package com.cuping.cupingbe.service;

import com.cuping.cupingbe.dto.*;
import jakarta.servlet.http.Cookie;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {

	private final UserRepository userRepository;
	private final OwnerPageService ownerPageService;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final RedisUtil redisUtil;

	public ResponseEntity<Message> signup(String type, MemberSignupRequestDto requestDto) throws Exception {

		UserRoleEnum role;
		switch (type) {
			case "user" -> role = UserRoleEnum.USER;
			case "admin" -> {
				role = UserRoleEnum.ADMIN;
				if (!jwtUtil.checkAdminKey(requestDto.getAdminKey()))
					throw new CustomException(ErrorCode.INVALID_ADMIN_KEY);
			}
			case "owner" -> role = UserRoleEnum.OWNER;
			default -> throw new CustomException(ErrorCode.INVALID_TYPE);
		}
		User user = new User(requestDto.getUserId(),
				passwordEncoder.encode(requestDto.getPassword()),
				requestDto.getNickname(), role);
		userRepository.save(user);
		if (type.equals("owner")) {
			ownerPageService.createCafe(new OwnerPageRequestDto(requestDto.getStoreName()
					, requestDto.getStoreAddress(), requestDto.getStoreNumber()
					, requestDto.getAuthImage()), user);
		}
		return new ResponseEntity<>(new Message("회원가입 성공", null), HttpStatus.OK);
	}

	public ResponseEntity<Message> duplicateCheckId(Map<String, String> userId) {
		if (userRepository.findByUserId(userId.get("userId")).isPresent())
			throw new CustomException(ErrorCode.DUPLICATE_IDENTIFIER);
		return new ResponseEntity<>(new Message("사용 가능한 아이디입니다.", null), HttpStatus.OK);
	}

	public ResponseEntity<Message> duplicateCheckNickname(Map<String, String> nickname) {
		if (userRepository.findByNickname(nickname.get("nickname")).isPresent())
			throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
		return new ResponseEntity<>(new Message("사용 가능한 닉네임입니다.", null), HttpStatus.OK);
	}

	public ResponseEntity<Message> login(MemberLoginRequestDto memberLoginRequestDto, HttpServletResponse response){
		String userId = memberLoginRequestDto.getUserId();

		User user = userRepository.findByUserId(userId).orElseThrow(
				() -> new CustomException(ErrorCode.INVALID_ID)
		);
		if(!passwordEncoder.matches(memberLoginRequestDto.getPassword(), user.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_PASSWORD);
		}
		TokenDto tokenDto = jwtUtil.creatAllToken(userId, user.getRole());
		if (redisUtil.get(userId).isEmpty()) {
			redisUtil.set(userId, tokenDto.getRefreshToken(), JwtUtil.REFRESH_TIME);
		} else {
			redisUtil.update(userId, tokenDto.getRefreshToken(), JwtUtil.REFRESH_TIME);
		}
		response.addHeader(JwtUtil.ACCESS_KEY,tokenDto.getAccessToken());
		response.addHeader(JwtUtil.REFRESH_KEY,tokenDto.getRefreshToken());
		return new ResponseEntity<>(new Message("로그인 성공", null), HttpStatus.OK);
	}

	public ResponseEntity<Message> logout(User user,HttpServletResponse response){

		String userId = user.getUserId();
		String refreshToken;
		if (redisUtil.get(userId).isPresent())
			refreshToken = redisUtil.get(userId).get().toString().substring(7);
		else
			throw new CustomException(ErrorCode.USER_NOT_FOUND);
		Long expireTime = jwtUtil.getExpirationTime(refreshToken);
		redisUtil.delete(userId);
		redisUtil.setBlackList(userId, refreshToken, expireTime);
		return new ResponseEntity<>(new Message("로그아웃 성공", null), HttpStatus.OK);
	}
}