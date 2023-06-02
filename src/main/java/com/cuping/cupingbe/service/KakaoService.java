package com.cuping.cupingbe.service;

import java.util.Optional;
import java.util.UUID;

import com.cuping.cupingbe.global.util.Message;
import org.apache.el.parser.Token;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.cuping.cupingbe.dto.KakaoUserInfoDto;
import com.cuping.cupingbe.dto.TokenDto;
import com.cuping.cupingbe.entity.User;
import com.cuping.cupingbe.entity.UserRoleEnum;
import com.cuping.cupingbe.global.jwt.JwtUtil;
import com.cuping.cupingbe.global.redis.util.RedisUtil;
import com.cuping.cupingbe.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	private final JwtUtil jwtUtil;
	private final RedisUtil redisUtil;

	public ResponseEntity<Message> kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {

		TokenDto tokenDto = getToken(code);
		KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(tokenDto.getAccessToken());
		User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);
		String userId = kakaoUser.getUserId();

		tokenDto = jwtUtil.creatAllToken(userId, kakaoUser.getRole());
		String access_token = tokenDto.getAccessToken();
		String refresh_token = tokenDto.getRefreshToken();
		if (redisUtil.get(userId).isEmpty()) {
			redisUtil.set(userId, refresh_token, JwtUtil.REFRESH_TIME);
		} else {
			redisUtil.update(userId, refresh_token, JwtUtil.REFRESH_TIME);
		}
		response.addHeader(JwtUtil.ACCESS_KEY, "Bearer " + access_token);
		response.addHeader(JwtUtil.REFRESH_KEY, "Bearer " + refresh_token);
		return new ResponseEntity<>(new Message("카카오 로그인 성공.", null), HttpStatus.OK);
	}

	private TokenDto getToken(String code) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", "826134c9ef39a5b494d322490e0e3abe");
		body.add("client_secret", "FVhCXvvHBKp8IhcvLIUy3exbWHiHIzMK");
		body.add("redirect_uri", "http://13.209.106.144:8080/oauth/kakao");
		body.add("code", code);

		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
			new HttpEntity<>(body, headers);
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> response = rt.exchange(
			"https://kauth.kakao.com/oauth/token",
			HttpMethod.POST,
			kakaoTokenRequest,
			String.class
		);

		String responseBody = response.getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(responseBody);
		return new TokenDto(jsonNode.get("access_token").asText(), jsonNode.get("refresh_token").asText());
	}

	private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> response = rt.exchange(
			"https://kapi.kakao.com/v2/user/me",
			HttpMethod.POST,
			kakaoUserInfoRequest,
			String.class
		);

		String responseBody = response.getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(responseBody);
		Long id = jsonNode.get("id").asLong();
		String nickname = jsonNode.get("properties")
			.get("nickname").asText();
		String email = jsonNode.get("kakao_account")
			.get("email").asText();
		String profile_image = jsonNode.get("properties")
			.get("profile_image").asText();

		return new KakaoUserInfoDto(id, nickname, email, profile_image);
	}

	private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
		Long kakaoId = kakaoUserInfo.getId();
		User kakaoUser = userRepository.findByKakaoId(kakaoId)
			.orElse(null);
		if (kakaoUser == null) {
			String kakaoEmail = kakaoUserInfo.getEmail();
			User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);
			if (sameEmailUser != null) {
				kakaoUser = sameEmailUser;
				kakaoUser = kakaoUser.kakaoIdUpdate(kakaoId);
			} else {
				String password = UUID.randomUUID().toString();
				String encodedPassword = passwordEncoder.encode(password);
				String email = kakaoUserInfo.getEmail();
				String image_url = kakaoUserInfo.getProfile_image();
				kakaoUser = new User(kakaoId.toString(), encodedPassword, kakaoUserInfo.getNickname(), UserRoleEnum.USER, kakaoId,
					email, image_url);
			}
			userRepository.save(kakaoUser);
		}
		return kakaoUser;
	}
}
