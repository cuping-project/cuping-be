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

        BooleanExpression condition = buildCondition(hashTagkey);

        JPAQuery<Bean> query = jpaQueryFactory.selectFrom(QBean.bean)
                .where(condition);

        setDesc(query, desc);

        beanList.addAll(query.fetch());

        return beanList;
    }

    private BooleanExpression buildCondition(String[] hashTagkey) {
        BooleanExpression condition = null;

        for (int i = 0; i < hashTagkey.length; i++) {
            String tag = "%" + hashTagkey[i] + "%";
            BooleanExpression tagCondition = QBean.bean.hashTag.like(tag);

            condition = mergeCondition(condition, tagCondition);
        }

        return condition;
    }

    private BooleanExpression mergeCondition(BooleanExpression condition, BooleanExpression tagCondition) {
        return (condition == null) ? tagCondition : condition.or(tagCondition);
    }

    private void setDesc(JPAQuery<Bean> query, boolean desc) {
        query.orderBy(desc ? QBean.bean.likesCount.desc() : QBean.bean.likesCount.asc());
    }

}
