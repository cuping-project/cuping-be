package com.cuping.cupingbe.service;

import com.cuping.cupingbe.dto.DetailPageResponseDto;
import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.global.exception.CustomException;
import com.cuping.cupingbe.global.exception.ErrorCode;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.repository.BeanRepository;
import com.cuping.cupingbe.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PageService {

    private final BeanRepository beanRepository;
    private final CafeRepository cafeRepository;

    public ResponseEntity<Message> getMainPage(Map<String, String> searchValue) {
        String roastingLevel = searchValue.get("roastingLevel");
        String origin = searchValue.get("origin");
        String flavor = searchValue.get("flavor");

        if (roastingLevel.equals(origin) && origin.equals(flavor)) {
            return new ResponseEntity<>(new Message("findAll", beanRepository.findAll()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Message("findFilter", beanRepository.findByRoastingLevelOrOriginOrFlavor(
                    roastingLevel, origin, flavor
            )), HttpStatus.OK);
        }
    }

    public ResponseEntity<Message> getSearchPage(String keyword) {
        if (keyword.isEmpty()) {
            return new ResponseEntity<>(new Message("Success", beanRepository.findAll()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Message("Success", beanRepository.findByBeanOriginNameContaining(keyword)),
            HttpStatus.OK);
        }
    }

    public ResponseEntity<Message> getDetailPage(Long cardId, String address) {
        Bean bean = beanRepository.findById(cardId).orElseThrow(
            () -> new CustomException(ErrorCode.INVALID_BEANS)
        );
        List<Cafe> cafeList = cafeRepository.findByBeanAndCafeAddressContaining(bean, address);
        return new ResponseEntity<>(new Message("Success", new DetailPageResponseDto(bean, cafeList)), HttpStatus.OK);
    }
}

