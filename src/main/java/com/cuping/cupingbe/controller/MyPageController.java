package com.cuping.cupingbe.controller;

import com.cuping.cupingbe.dto.MypageDto;
import com.cuping.cupingbe.dto.UserUpdateRequestDto;
import com.cuping.cupingbe.entity.User;
import com.cuping.cupingbe.global.exception.CustomException;
import com.cuping.cupingbe.global.exception.ErrorCode;
import com.cuping.cupingbe.global.security.UserDetailsImpl;
import com.cuping.cupingbe.service.MyPageService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class MyPageController {


    private MyPageService myPageService;

    @GetMapping("/mypage/{userId}")
    public ResponseEntity<MypageDto> getUserById(@PathVariable("userId") String userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails.getUser() == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        return myPageService.getUserById(userId);
    }

    @PatchMapping("/mypage/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable("userId") String userId,
                                        @RequestBody UserUpdateRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails.getUser() == null || !userDetails.getUser().getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        myPageService.updateUser(userId, requestDto);
        return new ResponseEntity<>("수정이 되었습니다.",HttpStatus.OK);
    }
}
