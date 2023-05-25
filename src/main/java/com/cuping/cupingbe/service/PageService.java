package com.cuping.cupingbe.service;

import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.global.exception.CustomException;
import com.cuping.cupingbe.global.exception.ErrorCode;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.repository.BeanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PageService {

    private final BeanRepository beanRepository;

    public ResponseEntity<Message> getMainPage(Map<String, String> searchValue) {
        String keyword = searchValue.get("keyword");
        String roastingLevel = searchValue.get("roasting");
        String origin = searchValue.get("origin");
        String flavor = searchValue.get("flavor");

        if (keyword.isEmpty() && roastingLevel.equals(origin) && origin.equals(flavor)) {
            return new ResponseEntity<>(new Message("Success", beanRepository.findAll()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Message("Success", beanRepository.findBySearch(
                    keyword, roastingLevel, origin, flavor
            )), HttpStatus.OK);
        }
    }

    public ResponseEntity<Message> getDetailPage(Long cardId, String address) {
        Bean bean = beanRepository.findById(cardId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_BEANS)
        );
        List<Cafe> cafeList = bean.getCafeList();
        List<Cafe> returnList = new ArrayList<>();
        for (Cafe c : cafeList) {
            if (c.getCafeAddress().contains(address))
                returnList.add(c);
        }
        bean.setCafeList(returnList);
        return new ResponseEntity<>(new Message("Success", bean), HttpStatus.OK);
    }
}
