package com.cuping.cupingbe.controller;

import com.cuping.cupingbe.dto.OwnerPageRequestDto;
import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.s3.S3Uploader;
import com.cuping.cupingbe.service.OwnerPageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class OwnerPageController {

    private final OwnerPageService ownerPageService;

    //카페 승인 요청
    @PostMapping(value = "/ownerpage/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> createCafe(@ModelAttribute OwnerPageRequestDto ownerPageRequestDto) throws Exception {
        return ownerPageService.createCafe(ownerPageRequestDto);
    }

}
