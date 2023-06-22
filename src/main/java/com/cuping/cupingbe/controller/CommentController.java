package com.cuping.cupingbe.controller;


import com.cuping.cupingbe.dto.CommentDeleteRequestDto;
import com.cuping.cupingbe.dto.CommentRequestDto;
import com.cuping.cupingbe.dto.CommentResponseDto;
import com.cuping.cupingbe.dto.CommentUpdateRequestDto;
import com.cuping.cupingbe.entity.Comment;
import com.cuping.cupingbe.entity.User;
import com.cuping.cupingbe.global.security.UserDetailsImpl;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    //댓글 작성
    @PostMapping("/comment/{beanId}")
    public ResponseEntity<Message> addComment(@PathVariable Long beanId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.addComment(beanId, commentRequestDto, userDetails.getUser());
    }

    //댓글 수정
    @PutMapping("/comment/{id}")
    public ResponseEntity<Message> updateComment(@PathVariable Long id, @RequestBody CommentUpdateRequestDto commentUpdateRequestDto
            , @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentUpdateRequestDto.setId(id);
        return commentService.updateComment(commentUpdateRequestDto, userDetails.getUser());
    }

    //댓글 삭제
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<Message> deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        CommentDeleteRequestDto commentDeleteRequestDto = new CommentDeleteRequestDto();
        commentDeleteRequestDto.setId(id);
        return commentService.deleteComment(commentDeleteRequestDto, userDetails.getUser());
    }
}
