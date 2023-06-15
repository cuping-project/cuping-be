package com.cuping.cupingbe.service;

import com.cuping.cupingbe.dto.MyPageDto;
import com.cuping.cupingbe.dto.UserUpdateRequestDto;
import com.cuping.cupingbe.entity.Likes;
import com.cuping.cupingbe.entity.User;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.repository.LikesRepository;
import com.cuping.cupingbe.repository.UserRepository;
import com.cuping.cupingbe.service.MyPageService;
import com.cuping.cupingbe.service.UtilService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.ClassFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyPageServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private LikesRepository likesRepository;

    @InjectMocks
    private MyPageService myPageService;

    @Mock
    private UtilService utilService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test //마이페이지 호출 테스트
    void getMyPageServiceTest() {
        // Given
        User user = new User();  // User 객체 초기화
        List<Likes> likesList = Arrays.asList(new Likes(), new Likes());  // Likes 객체 초기화

        given(likesRepository.findAllByUser(user)).willReturn(likesList);

        // When
        ResponseEntity<Message> responseEntity = myPageService.getMyPage(user);

        // Then
        assertSame(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void updateUser_UpdatesNicknameAndPassword() {
        // Given
        User user = new User();
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String nickname = "newNickname";
        user.setPassword(currentPassword);
        UserUpdateRequestDto requestDto = new UserUpdateRequestDto();
        requestDto.setCurrentPassword(currentPassword);
        requestDto.setNewPassword(newPassword);
        requestDto.setNickname(nickname);
        doNothing().when(utilService).checkUserPassword(requestDto.getCurrentPassword(), user.getPassword());
        when(passwordEncoder.encode(newPassword)).thenReturn(newPassword);

        // When
        myPageService.updateUser(user, requestDto);

        // Then
        assertEquals(nickname, user.getNickname());
        assertEquals(newPassword, user.getPassword());

        // Verify that checkUserPassword() was called
        verify(utilService).checkUserPassword(requestDto.getCurrentPassword(), currentPassword);
    }

}
