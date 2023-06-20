package com.cuping.cupingbe.repository.queryDsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomCafeRepositoryImpl implements CustomCafeRepository {

    private final JPAQueryFactory jpaQueryFactory;
}
