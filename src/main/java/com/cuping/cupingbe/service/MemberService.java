package com.cuping.cupingbe.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cuping.cupingbe.dto.MemberLoginRequestDto;
import com.cuping.cupingbe.dto.MemberSignupRequest;
import com.cuping.cupingbe.dto.TokenDto;
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

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final RedisUtil redisUtil;

	public ResponseEntity<Message> signup(String type, MemberSignupRequest memberSignupRequest){

		if(type.equals("user") || type.equals("owner")){
			String userId = memberSignupRequest.getUserId();
			String password = passwordEncoder.encode(memberSignupRequest.getPassword());
			String nickname = memberSignupRequest.getNickname();
			// if문 안에서는 UserRoleEnum이 선언이 안됨.
			UserRoleEnum role;
			if (type.equals("user"))
				role = UserRoleEnum.USER;
			else
				role = UserRoleEnum.OWNER;

			if (userRepository.findByUserId(userId).isPresent())
				throw new CustomException(ErrorCode.DUPLICATE_IDENTIFIER);
			if (userRepository.findByNickname(nickname).isPresent())
				throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);

			userRepository.save(new User(userId,password,nickname,role));
			if (type.equals("user"))
				return new ResponseEntity<>(new Message("user 회원가입 성공", null), HttpStatus.OK);

			String storeName = memberSignupRequest.getStoreName();
			String storeAddress = memberSignupRequest.getStoreAddress();
			String storeNumber = memberSignupRequest.getStoreNumber();
			String image = memberSignupRequest.getImage();

			// cafeCreate메서드 완성되면 추가.

			return new ResponseEntity<>(new Message("owner 회원가입 성공", null), HttpStatus.OK);

		} else if(type.equals("admin")) {
			String userId = memberSignupRequest.getUserId();
			String password = passwordEncoder.encode(memberSignupRequest.getPassword());
			String nickname = memberSignupRequest.getNickname();
			String adminKey = memberSignupRequest.getAdminKey();
			UserRoleEnum role = UserRoleEnum.ADMIN;

			if (userRepository.findByUserId(userId).isPresent())
				throw new CustomException(ErrorCode.DUPLICATE_IDENTIFIER);
			if (userRepository.findByNickname(nickname).isPresent())
				throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
			if (jwtUtil.checkAdminKey(adminKey)) {
				userRepository.save(new User(userId, password, nickname, role));
				return new ResponseEntity<>(new Message("admin 회원가입 성공", null), HttpStatus.OK);
			} else {
				throw new CustomException(ErrorCode.INVALID_ADMIN_KEY);
			}
		} else {
			throw new CustomException(ErrorCode.INVALID_TYPE);
		}
	}

	public ResponseEntity<Message> login(MemberLoginRequestDto memberLoginRequestDto, HttpServletResponse response){
		String userId = memberLoginRequestDto.getUserId();

		User user = userRepository.findByUserId(userId).orElseThrow(
			() -> new CustomException(ErrorCode.INVALID_ID_PASSWORD)
		);

		if(!passwordEncoder.matches(memberLoginRequestDto.getPassword(), user.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_ID_PASSWORD);
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
