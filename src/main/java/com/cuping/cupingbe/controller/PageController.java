package com.cuping.cupingbe.controller;

import com.cuping.cupingbe.global.util.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainPageController {

    private final MainPageService mainPageService;

    @GetMapping("/beans")
    public ResponseEntity<Message> mainPage(@RequestParam Map<String, String> searchValue) {
        return mainPageService.getMainPage(searchValue);
    }

    @GetMapping("/bean/{cardId}")
    public ResponseEntity<Message> detailPage(@PathVariable @RequestParam String address)
}
