package com.cuping.cupingbe.controller;

import com.cuping.cupingbe.dto.OwnerPageRequestDto;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.service.OwnerPageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cuping")
@RequiredArgsConstructor
public class OwnerPageController {

    private final OwnerPageService ownerPageService;

    @PostMapping("/ownerpage/register")
    public ResponseEntity<Message> createCafe(@RequestBody OwnerPageRequestDto ownerPageRequestDto) throws JsonProcessingException {
        return ownerPageService.createCafe(ownerPageRequestDto);
    }



}
