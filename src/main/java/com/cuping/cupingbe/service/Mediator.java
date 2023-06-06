package com.cuping.cupingbe.service;

import com.cuping.cupingbe.dto.OwnerPageRequestDto;
import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.entity.User;
import com.cuping.cupingbe.global.util.Message;
import org.springframework.http.ResponseEntity;

public interface Mediator {
    public User checkUserId(String userId);

    public void checkUserPassword(String inputPassword, String userPassword);

    public ResponseEntity<Message> createCafe(OwnerPageRequestDto ownerPageRequestDto, User user) throws Exception;

    public Bean checkBean(Long beanId);

    public Bean checkBean(String beanOriginName, String beanRoastingLevel, boolean isDuplicateCheck);

    public Cafe checkCafeId(Long cafeId);
}
