package com.cuping.cupingbe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
	Optional<RefreshToken> findByUserid(String userId);

		void deleteByUserid(String userId);
}
