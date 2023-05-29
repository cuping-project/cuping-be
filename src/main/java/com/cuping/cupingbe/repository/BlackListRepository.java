package com.cuping.cupingbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListRepository extends JpaRepository<BlackList,Long> {

	BlackList findByToken(String token);


}
