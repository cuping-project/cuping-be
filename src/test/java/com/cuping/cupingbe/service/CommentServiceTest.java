package com.cuping.cupingbe.service;

import com.cuping.cupingbe.dto.*;
import com.cuping.cupingbe.entity.*;
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
        User user = new User();

        Comment comment = new Comment();
        comment.setUser(user);

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        ResponseEntity<Message> response = commentService.deleteComment(requestDto, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(commentRepository, times(1)).delete(any(Comment.class));
    }
}
