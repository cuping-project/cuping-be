package com.cuping.cupingbe.repository;

import com.cuping.cupingbe.entity.Bean;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BeanRepository extends JpaRepository<Bean, Long> {

    public List<Bean> findByRoastingLevelOrOriginOrFlavor(String roastingLevel, String origin, String flavor);
    public List<Bean> findByBeanOriginNameContaining(String keyword);

    Bean findBybeanName(String beanName);
}
