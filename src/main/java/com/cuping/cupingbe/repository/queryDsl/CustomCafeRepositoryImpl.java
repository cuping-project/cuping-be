package com.cuping.cupingbe.repository.queryDsl;

import com.cuping.cupingbe.dto.CafeResponseDto;
import com.cuping.cupingbe.entity.Bean;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.cuping.cupingbe.entity.QCafe.cafe;

@RequiredArgsConstructor
public class CustomCafeRepositoryImpl implements CustomCafeRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CafeResponseDto> findByBeanAndCafeAddressContaining(Bean bean, String address) {
        return jpaQueryFactory.select(Projections.constructor(CafeResponseDto.class
                ,cafe.Id, cafe.cafeAddress,  cafe.cafePhoneNumber, cafe.cafeName
                , cafe.x, cafe.y, cafe.cafeImage, cafe.city, cafe.district, cafe.detailLink ))
                .from(cafe)
                .where(cafe.bean.eq(bean)
                        .and(cafe.district.eq(address)))
                .fetch();
    }
}
