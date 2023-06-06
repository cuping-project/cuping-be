package com.cuping.cupingbe.service;

import com.cuping.cupingbe.dto.OwnerPageRequestDto;
import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.entity.User;
import com.cuping.cupingbe.global.util.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MediatorImpl implements Mediator{

    private final MyPageService myPageService;
    private final OwnerPageService ownerPageService;
    private final PageService pageService;

    @Override
    public User checkUserId(String userId) {
        return myPageService.checkUserId(userId);
    }

    @Override
    public void checkUserPassword(String inputPassword, String userPassword) {
        myPageService.checkUserPassword(inputPassword, userPassword);
    }

    @Override
    public ResponseEntity<Message> createCafe(OwnerPageRequestDto ownerPageRequestDto, User user) throws Exception {
        return ownerPageService.createCafe(ownerPageRequestDto, user);
    }

    @Override
    public Bean checkBean(Long beanId) {
        return pageService.checkBean(beanId);
    }

    @Override
    public Bean checkBean(String beanOriginName, String beanRoastingLevel, boolean isDuplicateCheck) {
        return ownerPageService.checkBean(beanOriginName, beanRoastingLevel, isDuplicateCheck);
    }

    @Override
    public Cafe checkCafeId(Long cafeId) {
        return ownerPageService.checkCafeId(cafeId);
    }
}
