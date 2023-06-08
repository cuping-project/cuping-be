package com.cuping.cupingbe.service;

import com.cuping.cupingbe.dto.CommentDeleteRequestDto;
import com.cuping.cupingbe.dto.CommentRequestDto;
import com.cuping.cupingbe.dto.CommentResponseDto;
import com.cuping.cupingbe.dto.CommentUpdateRequestDto;
import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Comment;
import com.cuping.cupingbe.entity.User;
import com.cuping.cupingbe.entity.UserRoleEnum;
import com.cuping.cupingbe.global.exception.CustomException;
import com.cuping.cupingbe.global.exception.ErrorCode;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.repository.CommentRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UtilService utilService;

    //댓글 작성
    @Transactional
    public ResponseEntity<Message> addComment(Long beanId, CommentRequestDto commentRequestDto, User user) {
        Bean bean = utilService.checkBean(beanId);
        Comment comment = commentRepository.save(new Comment(commentRequestDto, user, bean));
        return new ResponseEntity<>(new Message("댓글 추가 성공.", new CommentResponseDto(comment)), HttpStatus.CREATED);
    }

    // 댓글 수정
    @Transactional
    public ResponseEntity<Message> updateComment(CommentUpdateRequestDto requestDto, User user) {
        Comment comment = checkComment(requestDto.getId());
        checkUser(comment, user);
        commentRepository.save(comment);
        return new ResponseEntity<>(new Message("댓글 수정 성공.", new CommentResponseDto(comment)), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public Comment checkComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NONEXISTENT_COMMENT));
    }

    public void checkUser(Comment comment, User user) {
        if (!Objects.equals(comment.getUser().getId(), user.getId()) && !user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(ErrorCode.FORBIDDEN_MEMBER);
        }
    }

    //댓글 삭제
    @Transactional
    public ResponseEntity<Message> deleteComment(CommentDeleteRequestDto requestDto, User user) {
        Comment comment = checkComment(requestDto.getId());
        checkUser(comment, user);
        commentRepository.delete(comment);
        return new ResponseEntity<>(new Message("댓글 삭제 성공.", null), HttpStatus.NO_CONTENT);
    }
}
