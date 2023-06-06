package com.cuping.cupingbe.service;

import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.entity.User;
import com.cuping.cupingbe.global.exception.CustomException;
import com.cuping.cupingbe.global.exception.ErrorCode;
import com.cuping.cupingbe.repository.BeanRepository;
import com.cuping.cupingbe.repository.CafeRepository;
import com.cuping.cupingbe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtilService {

    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;
    private final BeanRepository beanRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User checkUserId(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_ID)
        );
    }

    public void checkUserPassword(String inputPassword, String userPassword) {
        if (inputPassword == null || !passwordEncoder.matches(inputPassword, userPassword))
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
    }

    @Transactional(readOnly = true)
    public Cafe checkCafeId(Long cafeId) {
        return cafeRepository.findById(cafeId).orElseThrow(
                () -> new CustomException(ErrorCode.UNREGISTER_CAFE)
        );
    }

    @Transactional(readOnly = true)
    public Bean checkBean(String beanOriginName, String beanRoastingLevel, boolean isDuplicateCheck) {
        Optional<Bean> bean = beanRepository.findByBeanOriginNameAndRoastingLevel(
                beanOriginName, beanRoastingLevel);
        if (isDuplicateCheck) {
            if (bean.isPresent())
                throw new CustomException(ErrorCode.DUPLICATE_BEAN);
            return null;
        } else {
            if (bean.isEmpty())
                throw new CustomException(ErrorCode.UNREGISTER_BEAN);
            return bean.get();
        }
    }

    @Transactional(readOnly = true)
    public Bean checkBean(Long beanId) {
        return beanRepository.findById(beanId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_BEANS)
        );
    }
}
