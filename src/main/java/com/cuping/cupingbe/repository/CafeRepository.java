package com.cuping.cupingbe.repository;

import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.entity.Bean;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CafeRepository extends JpaRepository<Cafe, Long> {
    //승인되지 않은 카페 전체 조회
    List<Cafe> findAllByPermit(boolean b);
    List<Cafe> findByBeanAndCafeAddressContaining(Bean bean, String address);
    Cafe findByOwnerId(Long ownerId);
    //사장페이지 카페 조회
    List<Cafe> findAllByOwnerId(Long id);
}
