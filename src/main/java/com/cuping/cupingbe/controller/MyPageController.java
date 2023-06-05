package com.cuping.cupingbe.controller;

import com.cuping.cupingbe.dto.MyPageDto;
import com.cuping.cupingbe.dto.UserUpdateRequestDto;
import com.cuping.cupingbe.global.exception.CustomException;
import com.cuping.cupingbe.global.exception.ErrorCode;
import com.cuping.cupingbe.global.security.UserDetailsImpl;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.service.MyPageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class MyPageController {

    private MyPageService myPageService;

    @GetMapping("/mypage")
    public ResponseEntity<Message> getUserById(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.getUserById(userDetails.getUser());
    }

    @PatchMapping("/mypage")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        myPageService.updateUser(userDetails.getUser(), requestDto);
        return new ResponseEntity<>("수정이 되었습니다.",HttpStatus.OK);
    }
}
