package com.cuping.cupingbe.controller;

import com.cuping.cupingbe.dto.AdminPageRequestDto;
import com.cuping.cupingbe.dto.AdminPageResponseDto;
import com.cuping.cupingbe.global.security.UserDetailsImpl;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.service.AdminPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminPageService adminPageService;

    //(관리자페이지)원두 등록
    @PostMapping(value = "/admin/bean", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<Message> createBean(@ModelAttribute AdminPageRequestDto adminPageRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return adminPageService.createBean(adminPageRequestDto, userDetails);
    }

    //(관리자페이지)승인되지 않은 카페 조회
    @GetMapping("/admin")
    public List<AdminPageResponseDto> getNotPermitCafe(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return adminPageService.getPermitCafe(userDetails);
    }

    //(관리자페이지)카페 승인
    @PatchMapping("/admin/owner/{cafeId}")
    public ResponseEntity<Message> permitCafe(@PathVariable Long cafeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminPageService.permitCafe(cafeId,userDetails);
    }

    //(관리자페이지)원두 삭제
    @DeleteMapping("/admin/bean/{beanId}")
    public ResponseEntity<Message> deleteBean(@PathVariable Long beanId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminPageService.deleteBean(beanId, userDetails);
    }

}
