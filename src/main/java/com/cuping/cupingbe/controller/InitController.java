package com.cuping.cupingbe.controller;

import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.repository.CafeRepository;
import com.cuping.cupingbe.service.OwnerPageService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InitController {

    private final CafeRepository cafeRepository;
    private final OwnerPageService ownerPageService;

    String[] location= {
            "도봉구",
            "강북구",
            "노원구",
            "은평구",
            "서대문구",
            "종로구",
            "성북구",
            "동대문구",
            "중랑구",
            "강동구",
            "광진구",
            "성동구",
            "중구",
            "용산구",
            "마포구",
            "강서구",
            "양천구",
            "영등포구",
            "동작구",
            "서초구",
            "강남구",
            "송파구",
            "관악구",
            "금천구",
            "구로구"
    };

    @GetMapping("/api/check/{num}")
    public JsonNode check(@PathVariable int num) throws Exception {
        return ownerPageService.initCafe("도봉구", num);
    }
}
