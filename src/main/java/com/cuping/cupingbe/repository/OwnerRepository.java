package com.cuping.cupingbe.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner,Long> {
	Optional<Owner> findByOwnerId(String ownerId);

	Optional<Owner> findByNickname(String nickname);

	Optional<Owner> findByStoreName(String storename);


}
