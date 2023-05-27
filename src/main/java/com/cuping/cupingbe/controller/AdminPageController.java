package com.cuping.cupingbe.controller;

import com.cuping.cupingbe.dto.AdminPageRequestDto;
import com.cuping.cupingbe.dto.AdminPageResponseDto;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.service.AdminPageService;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminPageController {

    private final AdminPageService adminPageService;

    //(관리자페이지)원두 등록
    @PostMapping(value = "/admin/bean", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<Message> createBean(@ModelAttribute AdminPageRequestDto adminPageRequestDto) throws IOException {
        return adminPageService.createBean(adminPageRequestDto);
    }

    //(관리자페이지)승인되지 않은 카페 조회
    @GetMapping("/adminpage")
    public List<AdminPageResponseDto> getNotPermitCafe(){
        return adminPageService.getPermitCafe();
    }

    //(관리자페이지)카페 승인
    @PatchMapping("/admin/owner/{cafeId}")
    public ResponseEntity<Message> permitCafe(@PathVariable Long cafeId) {
        return adminPageService.permitCafe(cafeId);
    }

}
