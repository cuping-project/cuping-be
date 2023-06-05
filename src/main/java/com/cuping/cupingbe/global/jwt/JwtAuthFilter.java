package com.cuping.cupingbe.global.jwt;

import java.io.IOException;
import java.util.Arrays;

import jakarta.servlet.http.Cookie;
import org.springframework.security.core.Authentication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cuping.cupingbe.dto.SecurityExceptionDto;
import com.cuping.cupingbe.entity.User;
import com.cuping.cupingbe.global.redis.util.RedisUtil;
import com.cuping.cupingbe.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.cuping.cupingbe.global.security.WebSecurityConfig.PERMIT_URI;

@RequiredArgsConstructor
@Configuration
@WebFilter(urlPatterns = "/**")
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		// JWT 토큰을 해석하여 추출.
		String access_token = jwtUtil.resolveToken(request, jwtUtil.ACCESS_KEY);
		String refresh_token = jwtUtil.resolveToken(request, jwtUtil.REFRESH_KEY);
		String uri = request.getRequestURI();
		boolean permit = false;

		for (String s : PERMIT_URI) {
			if (uri.contains(s)) {
				permit = true;
				break;
			}
		}
		if (!permit) {
			if (jwtUtil.validateToken(access_token)) {
				setAuthentication(jwtUtil.getUserInfoFromToken(access_token));
			} else if (access_token == null) {
				jwtExceptionHandler(response, "refreshToken 주세요.", HttpStatus.UNAUTHORIZED.value());
				return ;
			} else if (refresh_token != null && jwtUtil.refreshTokenValid(refresh_token)) {
				//Refresh토큰으로 유저명 가져오기
				String username = jwtUtil.getUserInfoFromToken(refresh_token);
				//유저명으로 유저 정보 가져오기
				User user = userRepository.findByUserId(username).get();
				//새로운 ACCESS TOKEN 발급
				String newAccessToken = jwtUtil.createToken(username, user.getRole(), "Access");
				//Header에 ACCESS TOKEN 추가
				response.setHeader(JwtUtil.ACCESS_KEY, newAccessToken);
				setAuthentication(username);
			} else {
				jwtExceptionHandler(response, "Token Error.", HttpStatus.UNAUTHORIZED.value());
				return ;
			}
		}
		filterChain.doFilter(request, response);
	}

	// 인증 객체를 생성하여 SecurityContext에 설정
	public void setAuthentication (String username){
		Authentication authentication = jwtUtil.createAuthentication(username);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		// security가 만드는 securityContextHolder 안에 authentication을 넣음.
		// security가 sicuritycontextholder에서 인증 객체를 확인.
		// JwtAuthFilter에서 authentication을 넣어주면 UsernamePasswordAuthenticationFilter 내부에서 인증이 된 것을 확인하고 추가적인 작업을 진행하지 않음.
	}

	// JWT 예외 처리를 위한 응답 설정
	public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
		response.setStatus(statusCode);
		response.setContentType("application/json");
		try {
			// 예외 정보를 JSON 형태로 변환하여 응답에 작성
			String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(statusCode, msg));
			response.getWriter().write(json);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}