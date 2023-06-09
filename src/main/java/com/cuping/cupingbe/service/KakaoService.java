package com.cuping.cupingbe.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
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
	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String clientId;
	@Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
	private String clientSecret;

	public void kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {

		TokenDto kakaoToken = getToken(code);
		KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(kakaoToken.getAccessToken());
		User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);
		String userId = kakaoUser.getUserId();

		if (userId == null)
			throw new IllegalArgumentException();
		TokenDto tokenDto = jwtUtil.creatAllToken(userId, kakaoUser.getRole());
		redisUtil.set(userId, tokenDto.getRefreshToken(), JwtUtil.REFRESH_TIME);
		jwtUtil.setCookies(response, tokenDto);
	}

	private TokenDto getToken(String code) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", clientId);
		body.add("client_secret", clientSecret);
		body.add("redirect_uri", "https://api.cuping.net/users/oauth/kakao");
		body.add("code", code);

		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
			new HttpEntity<>(body, headers);
		ResponseEntity<String> response = new RestTemplate().exchange(
			"https://kauth.kakao.com/oauth/token",
			HttpMethod.POST,
			kakaoTokenRequest,
			String.class
		);

		JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
		return new TokenDto(jsonNode.get("access_token").asText(), jsonNode.get("refresh_token").asText());
	}

	private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
		ResponseEntity<String> response = new RestTemplate().exchange(
			"https://kapi.kakao.com/v2/user/me",
			HttpMethod.POST,
			kakaoUserInfoRequest,
			String.class
		);

		JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
		return new KakaoUserInfoDto(jsonNode);
	}

	private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
		Long kakaoId = kakaoUserInfo.getId();
		User kakaoUser = userRepository.findByKakaoId(kakaoId)
			.orElse(null);
		User returnUser;
		if (kakaoUser == null) {
			String kakaoEmail = kakaoUserInfo.getEmail();
			User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);
			if (sameEmailUser != null) {
				sameEmailUser.kakaoIdUpdate((kakaoId));
				returnUser = userRepository.save(sameEmailUser);
			} else {
				returnUser = userRepository.save(new User(kakaoId.toString(), passwordEncoder.encode(UUID.randomUUID().toString()),
						kakaoUserInfo.getNickname(), UserRoleEnum.USER, kakaoId, kakaoUserInfo.getEmail(), kakaoUserInfo.getProfile_image()));
			}
		} else
			returnUser = kakaoUser;
		return returnUser;
	}
}