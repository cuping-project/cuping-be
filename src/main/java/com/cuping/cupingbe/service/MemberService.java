package com.cuping.cupingbe.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cuping.cupingbe.dto.MemberLoginRequestDto;
import com.cuping.cupingbe.dto.MemberSignupRequest;
import com.cuping.cupingbe.dto.MemberResponseDto;
import com.cuping.cupingbe.dto.TokenDto;
import com.cuping.cupingbe.entity.User;
import com.cuping.cupingbe.entity.UserRoleEnum;
import com.cuping.cupingbe.global.exception.CustomException;
import com.cuping.cupingbe.global.exception.ErrorCode;
import com.cuping.cupingbe.global.jwt.JwtUtil;
import com.cuping.cupingbe.global.redis.util.RedisUtil;
import com.cuping.cupingbe.repository.OwnerRepository;
import com.cuping.cupingbe.repository.RefreshTokenRepository;
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
	private final RefreshTokenRepository refreshTokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final RedisUtil redisUtil;
	private final OwnerRepository ownerRepository;


	public MemberResponseDto signup(String type, MemberSignupRequest memberSignupRequest){

		if(type.equals("USER")){
			String userId = memberSignupRequest.getUserId();
			String password = passwordEncoder.encode(memberSignupRequest.getPassword());
			String nickname = memberSignupRequest.getNickname();
			UserRoleEnum role = UserRoleEnum.USER;





			Optional<User> findByUserId = userRepository.findByUserId(userId);
			Optional<User> findByNickname = userRepository.findByNickname(nickname);


			if(findByUserId.isPresent()){
				throw new CustomException(ErrorCode.DUPLICATE_IDENTIFIER);
			}

			if(findByNickname.isPresent()){
				throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
			}


			User user = new User(userId,password,nickname,role);
			userRepository.save(user);

			return new MemberResponseDto("회원가입성공");





		}
		else if(type.equals("ADMIN")){
			String userId = memberSignupRequest.getUserId();
			String password = passwordEncoder.encode(memberSignupRequest.getPassword());
			UserRoleEnum role = UserRoleEnum.ADMIN;

			Optional<User> findByUserId = userRepository.findByUserId(userId);

			if(findByUserId.isPresent()){
				throw new CustomException(ErrorCode.DUPLICATE_IDENTIFIER);
			}

			User user = new User(userId,password,role);
			userRepository.save(user);

			return new MemberResponseDto("회원가입성공");




		}
		else{
			String ownerId = memberSignupRequest.getOwnerId();
			String password = passwordEncoder.encode(memberSignupRequest.getPassword());
			String nickname = memberSignupRequest.getNickname();
			String storeName = memberSignupRequest.getStoreName();
			String storeAddress = memberSignupRequest.getStoreAddress();
			String storeNumber = memberSignupRequest.getStoreNumber();
			String image = memberSignupRequest.getImage();
			UserRoleEnum role = UserRoleEnum.OWNER;

			Optional<Owner> findByOwnerId = ownerRepository.findByOwnerId(ownerId);

			Optional<Owner> findByNickname = ownerRepository.findByNickname(nickname);

			Optional<Owner> findByStoreName = ownerRepository.findByStoreName(storeName);

			if(findByOwnerId.isPresent()){
				throw new CustomException(ErrorCode.DUPLICATE_IDENTIFIER);
			}
			if(findByNickname.isPresent()){
				throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
			}

			Owner owner = new Owner(ownerId,password,nickname,storeName,storeAddress,storeNumber,image,role);
			ownerRepository.save(owner);

			return new MemberResponseDto("회원가입성공");

		}






	}

	public MemberResponseDto login(MemberLoginRequestDto memberLoginRequestDto, HttpServletResponse response){
		String userId = memberLoginRequestDto.getUserId();
		String password = memberLoginRequestDto.getPassword();

		Optional<User> findByUserId = userRepository.findByUserId(userId);


		if(findByUserId.isEmpty()){
			throw new CustomException(ErrorCode.USER_NOT_FOUND);
		}

		if(!passwordEncoder.matches(password,findByUserId.get().getPassword())){
			throw new CustomException(ErrorCode.INVALID_PASSWORD);
		}

		TokenDto tokenDto = jwtUtil.creatAllToken(userId,findByUserId.get().getRole());
		Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserid(userId);

		if(refreshToken.isPresent()){
			refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
		}
		else{
			RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(),userId);
			refreshTokenRepository.save(newToken);
		}
		response.addHeader(JwtUtil.ACCESS_KEY,tokenDto.getAccessToken());
		response.addHeader(JwtUtil.REFRESH_KEY,tokenDto.getRefreshToken());

		return new MemberResponseDto("로그인 성공");
	}

	public MemberResponseDto logout(User user,HttpServletResponse response){
		Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserid(user.getUserId());
		String accessToken = response.getHeader("ACCESS_KEY").substring(7);
		if(refreshToken.isPresent()){
			Long tokenTime = jwtUtil.getExpirationTime(accessToken);
			redisUtil.setBlackList(accessToken,"access_token",tokenTime);
			refreshTokenRepository.deleteByUserid(user.getUserId());
		}
		throw new CustomException(ErrorCode.USER_NOT_FOUND);
	}



}
