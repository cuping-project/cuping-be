package com.cuping.cupingbe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cuping.cupingbe.entity.User;

public interface UserRepository extends JpaRepository<User,Long> {
	Optional<User> findByUserId(String userId);
	Optional<User> findByNickname(String nickname);

	Optional<User> findByKakaoId(Long kakaoId);
	Optional<User> findByEmail(String email);
}
