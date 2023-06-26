package com.cuping.cupingbe.repository.queryDsl;

import com.cuping.cupingbe.dto.CafeResponseDto;
import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Cafe;

import java.util.List;

public interface CustomCafeRepository {

    List<CafeResponseDto> findByBeanAndCafeAddressContaining(Bean bean, String cafeAddress);
}
