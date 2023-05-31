package com.cuping.cupingbe.controller;

import com.cuping.cupingbe.dto.AddBeanByCafeRequestDto;
import com.cuping.cupingbe.dto.OwnerPageRequestDto;
import com.cuping.cupingbe.dto.OwnerResponseDto;
import com.cuping.cupingbe.dto.OwnerResponseTotalDto;
import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.global.security.UserDetailsImpl;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.s3.S3Uploader;
import com.cuping.cupingbe.service.OwnerPageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OwnerPageController {

    private final OwnerPageService ownerPageService;

    //카페 승인 요청
    @PostMapping(value = "/ownerpage/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> createCafe(@ModelAttribute OwnerPageRequestDto ownerPageRequestDto,@AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
        return ownerPageService.createCafe(ownerPageRequestDto, userDetails.getUser());
    }

    //(사장님 페이지)카페 삭제
    @DeleteMapping("/ownerpage/resister/{cafeid}")
    public ResponseEntity<Message> deleteCafe(@PathVariable Long cafeid, @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
        return ownerPageService.deleteCafe(cafeid, userDetails.getUser());
    }

    //(사장님 페이지) 사장님이 소유한 카페 가져오기
    @GetMapping("/ownerpage")
    public OwnerResponseTotalDto getCafe(@AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
        return ownerPageService.getCafe(userDetails.getUser());
    }

    //(사장페이지) 카페에 원두 추가
    @PostMapping("/ownerpage/resister/bean/{cafeid}")
    public ResponseEntity<Message> addBeanByCafe(@PathVariable Long cafeid ,@RequestBody AddBeanByCafeRequestDto addBeanByCafeRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ownerPageService.addBeanByCafe(cafeid, addBeanByCafeRequestDto, userDetails.getUser());
    }
}
