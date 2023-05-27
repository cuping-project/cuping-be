package com.cuping.cupingbe.controller;

import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class PageController {

    private final PageService pageService;

    @GetMapping("/beans")
    public ResponseEntity<Message> mainPage(@RequestParam Map<String, String> searchValue) {
        return pageService.getMainPage(searchValue);
    }

    @GetMapping("beans/search")
    public ResponseEntity<Message> searchPage(@RequestParam String keyword) {
        return pageService.getSearchPage(keyword);
    }

    @GetMapping("/bean/{cardId}")
    public ResponseEntity<Message> detailPage(@PathVariable Long cardId, @RequestParam String address) {
        return pageService.getDetailPage(cardId, address);
    }
}
