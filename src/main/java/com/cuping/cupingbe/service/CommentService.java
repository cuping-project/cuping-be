//package com.cuping.cupingbe.service;
//
//import com.cuping.cupingbe.dto.CommentRequestDto;
//import com.cuping.cupingbe.dto.CommentResponseDto;
//import com.cuping.cupingbe.entity.Comment;
//import com.cuping.cupingbe.global.exception.ErrorCode;
//import com.cuping.cupingbe.repository.CommentRespository;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//
//@Service
//@RequiredArgsConstructor
//public class CommentService {
//
//    private final CommentRespository commentRepository;
//
//
//    //댓글 작성
//    @Transactional
//    public CommentResponseDto addComment(CommentRequestDto commentRequestDto, User user) {
//
//
//        User user = userRepository.findById(commentRequestDto.getUserId()).orElseThrow(
//                () -> new IllegalArgumentException("존재하지 않는 게시글입니다.")
//        );
//
//
//        Comment comment = new Comment(commentRequestDto, user, user.getUsername());
//        commentRepository.saveAndFlush(comment);
//        return new CommentResponseDto(comment);
//
//    }
//
//    //댓글 수정
//    @Transactional
//    public ResponseEntity updateComment(Long id, CommentRequestDto commentRequestDto, User user) {
//
//        UserRoleEnum userRoleEnum = user.getRole();
//        System.out.println("role = " + userRoleEnum);
//        if( userRoleEnum == UserRoleEnum.ADMIN) {
//            Comment comment = commentRepository.findById(id).orElseThrow(
//                    () -> new IllegalArgumentException("해당 댓글은 존재하지 않습니다.")
//            );
//            comment.update(commentRequestDto);
//            CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
//            return ResponseEntity.status(200).body(commentResponseDto);
//        }else {
//
//
//            Comment comment = commentRepository.findByIdAndUsername(id, user.getUsername()).orElseThrow(
//                    () -> new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.")
//            );
//            if (comment == null) {
//                ErrorCode errorCode = new ErrorCode("작성자만 삭제/수정할 수 있습니다.", 400);
//                return ResponseEntity.status(400).body(msgResponseDto);
//            } else {
//                comment.update(commentRequestDto);
//                CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
//                return ResponseEntity.status(200).body(commentResponseDto);
//            }
//        }
//    }
//
//    //댓글 삭제
//    @Transactional
//    public ResponseEntity deleteComment(Long id, User user) {
//
//        //사용자 권환 확인 (ADMUN인지 아닌지)
//        UserRoleEnum userRoleEnum = user.getRole();
//        System.out.println("role = " + userRoleEnum);
//        if (userRoleEnum == UserRoleEnum.ADMIN) {
//            Comment comment = commentRepository.findById(id).orElseThrow(
//                    () -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다.")
//            );
//            commentRepository.delete(comment);
//
//            return ResponseEntity.status(200).body("삭제성공");
//        } else {
//
//            Comment comment = commentRepository.findById(id).orElseThrow(
//                    () -> new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.")
//            );
//
//            commentRepository.delete(comment);
//            return ResponseEntity.status(200).body("삭제성공");
//        }
//    }
//}
