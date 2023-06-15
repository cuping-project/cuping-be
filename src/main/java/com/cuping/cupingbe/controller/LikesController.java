package com.cuping.cupingbe.controller;

import com.cuping.cupingbe.dto.LikesResponseDto;
import com.cuping.cupingbe.global.security.UserDetailsImpl;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    @PostMapping("/likes/{beanId}")
    public ResponseEntity<Message> toggleLikeStatus(@PathVariable Long beanId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likesService.toggleLikeStatus(beanId, userDetails.getUsername());
    }
}
