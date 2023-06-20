package com.cuping.cupingbe.repository.queryDsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomLikesRepositoryImpl implements CustomLikesRepository {

    private final JPAQueryFactory jpaQueryFactory;
}
