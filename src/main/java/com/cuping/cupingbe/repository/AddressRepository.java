package com.cuping.cupingbe.repository;

import com.cuping.cupingbe.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
