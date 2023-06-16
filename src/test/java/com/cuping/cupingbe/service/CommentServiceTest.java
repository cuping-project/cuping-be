package com.cuping.cupingbe.service;

import com.cuping.cupingbe.dto.*;
import com.cuping.cupingbe.entity.*;
import com.cuping.cupingbe.global.exception.CustomException;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CommentServiceTest {

    @InjectMocks
    CommentService commentService;

    @Mock
    CommentRepository commentRepository;

    @Mock
    UtilService utilService;



    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test //댓글 작성 테스트
    public void addCommentTest() {
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        User user = new User();
        Bean bean = new Bean();

        when(utilService.checkBean(anyLong())).thenReturn(bean);
        when(commentRepository.save(any(Comment.class))).thenReturn(new Comment());

        ResponseEntity<Message> response = commentService.addComment(1L, commentRequestDto, user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test //댓글 수정 테스트
    public void updateCommentTest() {
        CommentUpdateRequestDto requestDto = new CommentUpdateRequestDto();
        requestDto.setId(1L);  // 수정하려는 댓글의 ID
        requestDto.setContent("Updated content");  // 수정된 댓글 내용
        User user = new User();

        Comment comment = new Comment();
        comment.setUser(user);

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        ResponseEntity<Message> response = commentService.updateComment(requestDto, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test //댓글 삭제 테스트
    public void deleteCommentTest() {
        CommentDeleteRequestDto requestDto = new CommentDeleteRequestDto();
        requestDto.setId(1L);  // 수정하려는 댓글의 ID
        User user = new User();

        Comment comment = new Comment();
        comment.setUser(user);

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        ResponseEntity<Message> response = commentService.deleteComment(requestDto, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(commentRepository, times(1)).delete(any(Comment.class));
    }

    @Test
    public void checkCommentTest_Pass() {

        Comment comment = new Comment();
        // Given
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        // When
        Comment result = commentService.checkComment(1L);

        //Then
        assertNotNull(result, "해당 댓글이 존재하지 않습니다.");
        assertEquals(comment.getId(), result.getId());

        //verify
        verify(commentRepository, times(1)).findById(anyLong());

    }

    @Test
    public void checkCommentTest_Failed() {
        // Given
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());

        //Then
        assertThrows(CustomException.class, () -> commentService.checkComment(1L));

        //verify
        verify(commentRepository, times(1)).findById(anyLong());

    }

    @Test
    public void Admin_checkUserTest_Pass() {

        Comment comment = new Comment();
        User user = new User();
        //given
        user.setRole(UserRoleEnum.ADMIN);
        user.setUserId("1");
        comment.setUser(user);
        comment.getUser().setUserId("1");

        //then
        assertDoesNotThrow(() -> commentService.checkUser(comment,user));

    }

    @Test
    public void User_checkUserTest_Pass() {

        Comment comment = new Comment();
        User user = new User();
        //given
        user.setRole(UserRoleEnum.USER);
        user.setUserId("1");
        comment.setUser(user);
        comment.getUser().setUserId("1");

        //then
        assertDoesNotThrow(() -> commentService.checkUser(comment,user));

    }

    @Test
    public void User_checkUserTest_Failed() {

        Comment comment = new Comment();
        User user = new User();
        User otherUser = new User();
        //given
        user.setRole(UserRoleEnum.USER);
        user.setUserId("1");
        otherUser.setRole(UserRoleEnum.USER);
        otherUser.setUserId("3");
        comment.setUser(user);
        comment.getUser().setUserId("1");

        //then
        assertThrows(CustomException.class, () -> commentService.checkUser(comment,otherUser));

    }
}
