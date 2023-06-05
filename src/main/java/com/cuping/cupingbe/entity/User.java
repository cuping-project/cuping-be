package com.cuping.cupingbe.entity;

import com.cuping.cupingbe.dto.MemberSignupRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
@Setter
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String userId;

	@Column(nullable = false)
	private String nickname;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private UserRoleEnum role;

	@Column(nullable = true)
	private Long kakaoId;

	@Column(nullable = true)
	private String email;

	@Column(nullable = true)
	private String profile_image;

	//일반, 관리자 회원가입
	public User(String userId,String password,String nickname,UserRoleEnum role){
		this.userId = userId;
		this.password =password;
		this.nickname = nickname;
		this.role =role;
	}

	//카카오 생성자
	public User(String userId, String password, String nickname, UserRoleEnum role, Long kakaoId, String email, String profile_image) {
		this.userId = userId;
		this.password = password;
		this.nickname = nickname;
		this.role = role;
		this.kakaoId = kakaoId;
		this.email = email;
		this.profile_image = profile_image;

	}

	public User kakaoIdUpdate(Long kakaoId) {
		this.kakaoId = kakaoId;
		return this;
	}

	public User(MemberSignupRequestDto memberSignupRequestDto){
		this.userId = memberSignupRequestDto.getUserId();
		this.password = memberSignupRequestDto.getPassword();
	}

}
