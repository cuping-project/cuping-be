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
import com.cuping.cupingbe.repository.BeanRepository;
import com.cuping.cupingbe.repository.CommentRepository;
import com.cuping.cupingbe.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BeanRepository beanRepository;

    //댓글 작성
    @Transactional
    public ResponseEntity<CommentResponseDto> addComment(Long beanId, CommentRequestDto commentRequestDto, User user) {
        Bean bean = beanRepository.findById(beanId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_BEANS));
        Comment comment = new Comment(commentRequestDto, user, bean);
        commentRepository.save(comment);
        return new ResponseEntity<>(new CommentResponseDto(comment), HttpStatus.CREATED);
    }


    //댓글 수정
    @Transactional
    public ResponseEntity<CommentResponseDto> updateComment(CommentUpdateRequestDto requestDto, User user) {
        Comment comment = commentRepository.findById(requestDto.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NONEXISTENT_COMMENT));

        if (!comment.getUser().equals(user) && !user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_OWNER);
        }

        comment.updateContent(requestDto.getContent());
        return new ResponseEntity<>(new CommentResponseDto(comment), HttpStatus.OK);
    }

    //댓글 삭제
    @Transactional
    public ResponseEntity<Void> deleteComment(CommentDeleteRequestDto requestDto, User user) {
        Comment comment = commentRepository.findById(requestDto.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NONEXISTENT_COMMENT));

        if (!comment.getUser().equals(user) && !user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_OWNER);
        }

        commentRepository.delete(comment);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
