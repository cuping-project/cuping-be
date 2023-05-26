package com.cuping.cupingbe.controller;

import com.cuping.cupingbe.dto.AdminPageRequestDto;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.service.AdminPageService;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/cuping")
@RequiredArgsConstructor
public class AdminPageController {

    private final AdminPageService adminPageService;

    @PostMapping(value = "/admin/bean", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<Message> createBean(@ModelAttribute AdminPageRequestDto adminPageRequestDto) throws IOException {
        return adminPageService.createBean(adminPageRequestDto);
    }

}
