//package com.cuping.cupingbe.controller;
//
//
//import com.cuping.cupingbe.dto.CommentRequestDto;
//import com.cuping.cupingbe.dto.CommentResponseDto;
//import com.cuping.cupingbe.entity.Comment;
//import com.cuping.cupingbe.service.CommentService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.security.core.Authentication;
//
//@RestController
//@RequiredArgsConstructor
//public class CommentController {
//
//    //댓글 작성
//    private final CommentService commentService;
//
//    @PostMapping("/comment")
//    public CommentResponseDto addComment(@RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
//        return commentService.addComment(commentRequestDto,userDetails.getUser());
//    }
//
//    //댓글 수정
//    @PutMapping("/comment/{id}")
//    public ResponseEntity updateComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
//        return commentService.updateComment(id, commentRequestDto, userDetails.getUser());
//    }
//
//    //댓글 삭제
//    @DeleteMapping("/comment/{id}")
//    public  ResponseEntity deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
//        return commentService.deleteComment(id, userDetails.getUser());
//    }
//
//
//
//
//
//
//}
