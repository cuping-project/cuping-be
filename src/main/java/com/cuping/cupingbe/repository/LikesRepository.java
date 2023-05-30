package com.cuping.cupingbe.repository;

import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Likes;
import com.cuping.cupingbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Likes findByUserAndBean(User user, Bean bean);

    List<Likes> findAllByBean(Bean bean);
}