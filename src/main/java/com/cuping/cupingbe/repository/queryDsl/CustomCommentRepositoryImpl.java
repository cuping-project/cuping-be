package com.cuping.cupingbe.repository.queryDsl;

import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Comment;
import com.cuping.cupingbe.entity.QComment;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private static final int PAGE_SIZE = 4;

    @Override
    public Optional<Comment> findById(Long id) {
        Comment comment = jpaQueryFactory.selectFrom(QComment.comment)
                .where(QComment.comment.id.eq(id))
                .fetchOne();
        return Optional.ofNullable(comment);
    }

    @Override
    public List<Comment> findByBean(Bean bean, int page) {
        return jpaQueryFactory.selectFrom(QComment.comment)
                .where(QComment.comment.bean.eq(bean))
                .offset((long) (page - 1) * PAGE_SIZE)
                .limit(PAGE_SIZE)
                .fetch();
    }


}
