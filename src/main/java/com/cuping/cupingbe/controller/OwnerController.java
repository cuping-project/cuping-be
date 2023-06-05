package com.cuping.cupingbe.controller;

import com.cuping.cupingbe.dto.*;
import com.cuping.cupingbe.global.security.UserDetailsImpl;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.service.OwnerPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerPageService ownerPageService;

    //카페 승인 요청
    @PostMapping(value = "/owner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> createCafe(@ModelAttribute OwnerPageRequestDto ownerPageRequestDto,@AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
        return ownerPageService.createCafe(ownerPageRequestDto, userDetails.getUser());
    }

    //(사장님 페이지)카페 삭제
    @DeleteMapping("/owner/{cafeId}")
    public ResponseEntity<Message> deleteCafe(@PathVariable Long cafeId, @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
        return ownerPageService.deleteCafe(cafeId, userDetails.getUser());
    }

    //(사장님 페이지) 사장님이 소유한 카페 가져오기
    @GetMapping("/owner")
    public ResponseEntity<Message> getCafe(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ownerPageService.getCafe(userDetails.getUser());
    }

    //(사장페이지) 카페에 원두 추가
    @PostMapping("/owner/bean")
    public ResponseEntity<Message> addBeanByCafe(@RequestBody BeanByCafeRequestDto beanByCafeRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ownerPageService.addBeanByCafe(beanByCafeRequestDto, userDetails.getUser());
    }

    //(사장페이지) 카페에 등록된 원두 삭제
    @DeleteMapping("/owner/bean")
    public ResponseEntity<Message> deleteBeanByCafe(@RequestBody BeanByCafeRequestDto beanByCafeRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ownerPageService.deleteBeanByCafe(beanByCafeRequestDto, userDetails.getUser());
    }
}
