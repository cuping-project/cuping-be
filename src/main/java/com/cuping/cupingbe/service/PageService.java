package com.cuping.cupingbe.service;

import com.cuping.cupingbe.dto.DetailPageResponseDto;
import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.entity.Comment;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.repository.BeanRepository;
import com.cuping.cupingbe.repository.CafeRepository;
import com.cuping.cupingbe.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PageService {

    private final BeanRepository beanRepository;
    private final CafeRepository cafeRepository;
    private final CommentRepository commentRepository;
    private final UtilService utilService;

    // 메인페이지
    public ResponseEntity<Message> getMainPage(Map<String, String> searchValue) {
        String roastingLevel = searchValue.get("roastingLevel");
        String origin = searchValue.get("origin");
        String flavor = searchValue.get("flavor");

        if ("*".equals(roastingLevel) && roastingLevel.equals(origin) && origin.equals(flavor)) {
            return new ResponseEntity<>(new Message("findAll", beanRepository.findAll()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Message("findFilter", beanRepository.findByRoastingLevelOrOriginOrFlavor(
                    roastingLevel, origin, flavor
            )), HttpStatus.OK);
        }
    }

    // 검색
    public ResponseEntity<Message> getSearchPage(String keyword) {
        List<Bean> beanList;
        if (keyword.isEmpty()) {
            beanList = beanRepository.findAll();
        } else {
            beanList = beanRepository.findByBeanOriginNameContaining(keyword);
        }
        return new ResponseEntity<>(new Message("Success", beanList), HttpStatus.OK);
    }

    // 상세페이지
    public ResponseEntity<Message> getDetailPage(Long cardId, String address) {
        Bean bean = utilService.checkBean(cardId);
        List<Cafe> cafeList = setDetailPageCafe(bean, address);
        // Bean에 연결된 Comment 목록을 가져오기
        List<Comment> commentList = setDetailPageComment(bean);
        return new ResponseEntity<>(new Message("Success", new DetailPageResponseDto(bean, cafeList, commentList)), HttpStatus.OK);
    }

    public List<Cafe> setDetailPageCafe(Bean bean, String address) {
        return cafeRepository.findByBeanAndCafeAddressContaining(bean, address);
    }

    public List<Comment> setDetailPageComment(Bean bean) {
        return commentRepository.findByBean(bean);
    }
}

