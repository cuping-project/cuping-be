package com.cuping.cupingbe.service;

import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.entity.User;
import com.cuping.cupingbe.global.exception.CustomException;
import com.cuping.cupingbe.global.exception.ErrorCode;
import com.cuping.cupingbe.repository.BeanRepository;
import com.cuping.cupingbe.repository.CafeRepository;
import com.cuping.cupingbe.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UtilServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private CafeRepository cafeRepository;
    @Mock
    private BeanRepository beanRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UtilService utilService;

    @Test
    @DisplayName("checkUserIdFalse")
    void checkUserIdFalse() {
        // given
        String userId = "False";
        when(userRepository.findByUserId(userId)).thenThrow(new CustomException(ErrorCode.INVALID_ID));

        // when && then
        assertThrows(CustomException.class, () -> {
            utilService.checkUserId(userId);
        });
    }

    @Test
    @DisplayName("checkUserIdTrue")
    void checkUserIdTrue() {
        // given
        String userId = "True";
        User user = new User();
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));

        // when
        User responseUser = utilService.checkUserId(userId);

        // then
        assertThat(responseUser).isEqualTo(user);
    }

    @Test
    @DisplayName("checkUserPasswordInputIsNull")
    void checkUserPasswordInputIsNull() {
        // given
        String userPassword = "User";

        // when && then
        assertThrows(CustomException.class, () -> {
            utilService.checkUserPassword(null, userPassword);
        });
    }

    @Test
    @DisplayName("checkUserPasswordInputNotMatches")
    void checkUserPasswordInputNotMatches() {
        // given
        String inputPassword = "Test";
        String userPassword = "User";

        // when && then
        assertThrows(CustomException.class, () -> {
            utilService.checkUserPassword(inputPassword, userPassword);
        });
    }

    @Test
    @DisplayName("checkUserPasswordSuccess")
    void checkUserPasswordSuccess() {
        // given
        String inputPassword = "TestUser";
        String userPassword = "TestUser";
        when(passwordEncoder.matches(inputPassword, userPassword)).thenReturn(true);

        // when && then
        assertDoesNotThrow(() -> utilService.checkUserPassword(inputPassword, userPassword));
    }

    @Test
    @DisplayName("checkCafeIdFalse")
    void checkCafeIdFalse() {
        //given
        Long cafeId = 1L;

        // when && then
        assertThrows(CustomException.class, () -> {
            utilService.checkCafeId(cafeId);
        });
    }

    @Test
    @DisplayName("checkCafeIdTrue")
    void checkCafeIdTrue() {
        // given
        Long cafeId = 1L;
        Cafe cafe = new Cafe();
        when(cafeRepository.findById(cafeId)).thenReturn(Optional.of(cafe));

        // when
        Cafe responseCafe = utilService.checkCafeId(cafeId);

        assertThat(responseCafe).isEqualTo(cafe);
    }

    @Test
    @DisplayName("checkBeanBooleanFalseCheckFalse")
    void checkBeanBooleanFalseCheckFalse() {
        // given
        String beanOriginName = "Test";
        String beanRoastingLevel = "Level";
        when(beanRepository.findByBeanOriginNameAndRoastingLevel(beanOriginName, beanRoastingLevel))
                .thenThrow(new CustomException(ErrorCode.INVALID_BEANS));

        // when && then
        assertThrows(CustomException.class, () -> {
            utilService.checkBean(beanOriginName, beanRoastingLevel, false);
        });
    }

    @Test
    @DisplayName("checkBeanBooleanFalseCheckTrue")
    void checkBeanBooleanFalseCheckTrue() {
        // given
        String beanOriginName = "Test";
        String beanRoastingLevel = "Level";
        when(beanRepository.findByBeanOriginNameAndRoastingLevel(beanOriginName, beanRoastingLevel))
                .thenThrow(new CustomException(ErrorCode.INVALID_BEANS));

        // when && then
        assertThrows(CustomException.class, () -> {
            utilService.checkBean(beanOriginName, beanRoastingLevel, true);
        });
    }

    @Test
    @DisplayName("checkBeanBooleanTrueCheckFalse")
    void checkBeanBooleanTrueCheckFalse() {
        // given
        String beanOriginName = "Test";
        String beanRoastingLevel = "Level";
        Bean bean = new Bean();
        when(beanRepository.findByBeanOriginNameAndRoastingLevel(beanOriginName, beanRoastingLevel))
                .thenReturn(Optional.of(bean));

        // when && then
        assertThat(utilService.checkBean(beanOriginName, beanRoastingLevel, false))
                .isEqualTo(bean);
    }

    @Test
    @DisplayName("checkBeanBooleanTrueCheckTrue")
    void checkBeanBooleanTrueCheckTrue() {
        // given
        String beanOriginName = "Test";
        String beanRoastingLevel = "Level";
        Bean bean = new Bean();
        when(beanRepository.findByBeanOriginNameAndRoastingLevel(beanOriginName, beanRoastingLevel))
                .thenReturn(Optional.of(bean));

        // when && then
        assertThrows(CustomException.class, () -> {
            utilService.checkBean(beanOriginName, beanRoastingLevel, true);
        });
    }

    @Test
    @DisplayName("checkByBeanOriginNameAndRoastingLevelFalse")
    void checkByBeanOriginNameAndRoastingLevelFalse() {
        // given
        String beanOriginName = "Test";
        String beanRoastingLevel = "Level";
        when(beanRepository.findByBeanOriginNameAndRoastingLevel(beanOriginName, beanRoastingLevel))
                .thenThrow(new CustomException(ErrorCode.INVALID_BEANS));

        // when && then
        assertThrows(CustomException.class, () -> {
            utilService.checkByBeanOriginNameAndRoastingLevel(beanOriginName, beanRoastingLevel);
        });
    }

    @Test
    @DisplayName("checkByBeanOriginNameAndRoastingLevelTrue")
    void checkByBeanOriginNameAndRoastingLevelTrue() {
        // given
        String beanOriginName = "Test";
        String beanRoastingLevel = "Level";
        Bean bean = new Bean();
        when(beanRepository.findByBeanOriginNameAndRoastingLevel(beanOriginName, beanRoastingLevel))
                .thenReturn(Optional.of(bean));

        // when && then
        assertThat(utilService.checkByBeanOriginNameAndRoastingLevel(beanOriginName, beanRoastingLevel))
                .isEqualTo(Optional.of(bean));
    }

    @Test
    @DisplayName("checkBeanFalse")
    void checkBeanFalse() {
        // given
        Long beanId = 1L;
        when(beanRepository.findById(beanId)).thenThrow(new CustomException(ErrorCode.INVALID_BEANS));

        // when && then
        assertThrows(CustomException.class, () -> {
            utilService.checkBean(beanId);
        });
    }

    @Test
    @DisplayName("checkBeanTrue")
    void checkBeanTrue() {
        // given
        Long beanId = 1L;
        Bean bean = new Bean();
        when(beanRepository.findById(beanId)).thenReturn(Optional.of(bean));

        // when && then
        assertThat(utilService.checkBean(beanId)).isEqualTo(bean);
    }
}