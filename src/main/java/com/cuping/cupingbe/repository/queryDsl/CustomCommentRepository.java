package com.cuping.cupingbe.repository.queryDsl;

import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CustomCommentRepository {
    Optional<Comment> findById(Long id);
//    List<Comment> findByBean(Bean bean);

    List<Comment> findByBean(Bean bean, int page);

}
