package com.cuping.cupingbe.repository.queryDsl;

import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import static com.cuping.cupingbe.entity.QComment.comment;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Comment> findById(Long id) {
        return null;
    }

    @Override
    public List<Comment> findByBean(Bean bean) {
        return null;
    }
}
