package com.cuping.cupingbe.likes;

import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Likes;
import com.cuping.cupingbe.entity.User;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.repository.LikesRepository;
import com.cuping.cupingbe.service.LikesService;
import com.cuping.cupingbe.service.UtilService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class LikesServiceTest {

    @Mock
    private LikesRepository likesRepository;

    @Mock
    private UtilService utilService;

    @InjectMocks
    private LikesService likesService;

    @Test // 좋아요 누른상태에서 테스트
    public void toggleLikeStatus_LikesTrue() {
        // Given
        Long beanId = 1L;
        String userId = "testUser";

        Bean bean = new Bean();
        User user = new User();
        Likes likes = mock(Likes.class);  // Likes 객체를 목(Mock) 객체로 만듬.

        given(likes.isLikeStatus()).willReturn(true);  // Likes의 초기 상태 설정.
        given(utilService.checkBean(beanId)).willReturn(bean);
        given(utilService.checkUserId(userId)).willReturn(user);
        given(likesRepository.findByUserAndBean(user, bean)).willReturn(Optional.of(likes));

        // When
        ResponseEntity<Message> responseEntity = likesService.toggleLikeStatus(beanId, userId);

        // Then
        assertSame(responseEntity.getStatusCode(), HttpStatus.OK);
        verify(likes, times(1)).setLikeStatus();
    }

    @Test //좋아요 안눌린 상태에서 테스트
    public void toggleLikeStatus_LikesFalse() {
        // Given
        Long beanId = 1L;
        String userId = "testUser";

        Bean bean = new Bean();
        User user = new User();
        Likes likes = mock(Likes.class);  // Likes 객체를 목(Mock) 객체로 만듬.

        given(likes.isLikeStatus()).willReturn(false);  // Likes의 초기 상태 설정.
        given(utilService.checkBean(beanId)).willReturn(bean);
        given(utilService.checkUserId(userId)).willReturn(user);
        given(likesRepository.findByUserAndBean(user, bean)).willReturn(Optional.of(likes));

        // When
        ResponseEntity<Message> responseEntity = likesService.toggleLikeStatus(beanId, userId);

        // Then
        assertSame(responseEntity.getStatusCode(), HttpStatus.OK);
        verify(likes, times(1)).setLikeStatus();
    }

}