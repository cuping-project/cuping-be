package com.cuping.cupingbe.controller;

import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.repository.AddressRepository;
import com.cuping.cupingbe.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class PageController {

    private final PageService pageService;
    private final AddressRepository addressRepository;

//    @GetMapping("/beans")
//    public ResponseEntity<Message> mainPage(@RequestParam Map<String, String> searchValue) {
//        return pageService.getMainPage(searchValue);
//    }

    @GetMapping("beans/search")
    public ResponseEntity<Message> searchPage(@RequestParam String keyword) {
        return pageService.getSearchPage(keyword);
    }

    @GetMapping("beans/search/hashTag")
    public ResponseEntity<Message>  searchBean(@RequestParam boolean desc, String hashTag) {
        if (hashTag == null) {return pageService.getSearchPage(null);}
        else {return pageService.getKewordBean(desc, hashTag);}
    }

    @GetMapping("/bean/{cardId}")
    public ResponseEntity<Message> detailPage(@PathVariable Long cardId, @RequestParam String address, Integer pageNumber) {
        if(pageNumber != null) {return pageService.getDetailPage(cardId, address, pageNumber);}
        else {return  pageService.getDetailBean(cardId, address);}
    }

    @GetMapping("/address")
    public ResponseEntity<Message> addressList() {
        return new ResponseEntity<>(new Message("주소 목록", addressRepository.findAll()), HttpStatus.OK);
    }
}

