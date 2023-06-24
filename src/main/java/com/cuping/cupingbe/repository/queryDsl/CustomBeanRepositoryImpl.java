package com.cuping.cupingbe.repository.queryDsl;

import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CustomBeanRepositoryImpl implements CustomBeanRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Bean> findByBeanName(boolean desc, String[] hashTagkey) {
        List<Bean> beanList = new ArrayList<>();

        BooleanExpression condition = null;

        for (int i = 1; i <hashTagkey.length ; i++) {
            String tag = "%" + hashTagkey[i] + "%";
            BooleanExpression tagCondition = QBean.bean.hashTag.like(tag);

            if (condition == null) {
                condition = tagCondition;
            } else {
                condition = condition.or(tagCondition);
            }
        }

        JPAQuery<Bean> query = jpaQueryFactory.selectFrom(QBean.bean)
                .where(condition);

        if (desc) {
            query = query.orderBy(QBean.bean.likesCount.desc());
        } else {
            query = query.orderBy(QBean.bean.likesCount.asc());
        }

        beanList.addAll(query.fetch());

        return beanList;
    }


}
