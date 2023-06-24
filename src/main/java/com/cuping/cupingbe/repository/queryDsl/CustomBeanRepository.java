package com.cuping.cupingbe.repository.queryDsl;

import com.cuping.cupingbe.entity.Bean;

import java.util.List;

public interface CustomBeanRepository {
    List<Bean> findByBeanName(boolean desc, String[] key);

}
