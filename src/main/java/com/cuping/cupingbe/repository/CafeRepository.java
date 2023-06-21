package com.cuping.cupingbe.repository;

import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.repository.queryDsl.CustomCafeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CafeRepository extends JpaRepository<Cafe, Long>, CustomCafeRepository {
    //승인되지 않은 카페 전체 조회
    List<Cafe> findAllByPermit(boolean b);
    List<Cafe> findByBeanAndCafeAddressContaining(Bean bean, String cafeAddress);
    //사장페이지 카페 조회
    List<Cafe> findAllByOwnerId(Long id);
    Optional<Cafe> findByCafeAddress(String cafeAddress);
    Optional<Cafe> findFirstByCafeAddressAndOwnerId(String cafeAddress, Long userId);
    Optional<Cafe> findByCafeAddressAndBeanIdAndOwnerId(String cafeAddress, Long beanId, Long userId);
    Optional<Cafe> findByOwnerIdAndId(Long ownerId, Long cafeId);
    Optional<Cafe> findById(Long cafeID);
}
