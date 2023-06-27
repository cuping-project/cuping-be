package com.cuping.cupingbe.repository.queryDsl;

import com.cuping.cupingbe.dto.BeanRequestDto;
import com.cuping.cupingbe.entity.Bean;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

import static com.cuping.cupingbe.entity.QBean.bean;
import static io.jsonwebtoken.lang.Strings.hasText;

@RequiredArgsConstructor
public class CustomBeanRepositoryImpl implements CustomBeanRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Bean> findBeanByRequestDto(String keyword, String sort, String [] filter) {

        return jpaQueryFactory.selectFrom(bean)
                .where(keyword(keyword),
                        filter(filter))
                .orderBy(sort(sort))
                .fetch();
    }

    private BooleanExpression keyword (String keyword) {
        return hasText(keyword) ? bean.beanOriginName.contains(keyword) : null;
    }

    private BooleanExpression filter (String [] filterList) {
        return filterList != null ? Expressions.allOf(Arrays.stream(filterList).map(this::eqFilter).toArray(BooleanExpression[]::new)) : null;
    }

    private BooleanExpression eqFilter(String filter) {
        switch (filter) {
            case "bitter" -> {
                return bean.bitter.isTrue();
            }
            case "burnt" -> {
                return bean.burnt.isTrue();
            }
            case "sour" -> {
                return bean.sour.isTrue();
            }
            case "sweet" -> {
                return bean.sweet.isTrue();
            }
            default -> {
                return null;
            }
        }
    }

    private OrderSpecifier<?> sort (String sort) {
        if (sort.equals("likes"))
            return bean.likesCount.desc();
        else if (sort.equals("abc"))
            return bean.beanOriginName.asc();
        else
            return null;
    }
}
