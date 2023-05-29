package com.cuping.cupingbe.entity;

import com.cuping.cupingbe.dto.MemberSignupRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String userId;

	@Column(nullable = true)
	private String nickname;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private UserRoleEnum role;

	@Column(nullable = true)
	private Long kakaoid;

	@Column(nullable = true)
	private String email;

	@Column(nullable = true)
	private String profile_image;

	@Column(nullable = true)
	private String username;

	//일반 회원가입
	public User(String userId,String password,String nickname,UserRoleEnum role){
		this.userId = userId;
		this.password =password;
		this.nickname = nickname;
		this.role =role;
	}

	public User(String userId,String password,UserRoleEnum role){
		this.userId =userId;
		this.password =password;
		this.role = role;
	}

	//카카오 생성자
	public User(String userId, String password, String username, UserRoleEnum role, Long kakaoid, String email, String profile_image) {
		this.userId = userId;
		this.password = password;
		this.username = username;
		this.role = role;
		this.kakaoid = kakaoid;
		this.email = email;
		this.profile_image = profile_image;

	}

	public User kakaoIdUpdate(Long kakaoid) {
		this.kakaoid = kakaoid;
		return this;
	}

	public User(MemberSignupRequest memberSignupRequest){
		this.userId = memberSignupRequest.getUserId();
		this.password = memberSignupRequest.getPassword();
	}

}
