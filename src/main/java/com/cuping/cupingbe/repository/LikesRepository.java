package com.cuping.cupingbe.repository;

import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Likes;
import com.cuping.cupingbe.entity.User;
import com.cuping.cupingbe.repository.queryDsl.CustomLikesRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long>, CustomLikesRepository {
    Optional<Likes> findByUserAndBean(User user, Bean bean);

    List<Likes> findAllByBean(Bean bean);

    List<Likes> findAllByUser(User user);
}