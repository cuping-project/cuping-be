package com.cuping.cupingbe.repository.queryDsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomBeanRepositoryImpl implements CustomBeanRepository {

    private final JPAQueryFactory jpaQueryFactory;
}
