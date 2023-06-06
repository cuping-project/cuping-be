package com.cuping.cupingbe.repository;

import com.cuping.cupingbe.entity.Bean;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BeanRepository extends JpaRepository<Bean, Long> {

    public List<Bean> findByRoastingLevelOrOriginOrFlavor(String roastingLevel, String origin, String flavor);
    public List<Bean> findByBeanOriginNameContaining(String keyword);
    public Optional<Bean> findByBeanOriginNameAndRoastingLevel(String beanName, String roastingLevel);
}
