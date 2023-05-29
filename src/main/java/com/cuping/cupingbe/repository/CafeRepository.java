package com.cuping.cupingbe.repository;

import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.entity.Bean;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, Long> {
    //승인되지 않은 카페 전체 조회
    List<Cafe> findAllByPermit(boolean b);
    List<Cafe> findByBeanAndCafeAddressContaining(Bean bean, String address);
}
