package com.cuping.cupingbe.repository;

import com.cuping.cupingbe.entity.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BeanRepository extends JpaRepository<Bean, Long> {

    @Query("")
    public List<Bean> findBySearch(String keyword, String roastingLevel, String origin, String flavor);
}
