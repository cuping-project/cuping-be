package com.cuping.cupingbe.service;

import com.cuping.cupingbe.dto.BeanRequestDto;
import com.cuping.cupingbe.dto.CafeResponseDto;
import com.cuping.cupingbe.dto.DetailPageResponseDto;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PageService {

    private final BeanRepository beanRepository;
    private final CafeRepository cafeRepository;
    private final CommentRepository commentRepository;
    private final UtilService utilService;

    // 메인페이지
//    public ResponseEntity<Message> getMainPage(Map<String, String> searchValue) {
//        String roastingLevel = searchValue.get("roastingLevel");
//        String origin = searchValue.get("origin");
//        String flavor = searchValue.get("flavor");
//
//        if ("*".equals(roastingLevel) && roastingLevel.equals(origin) && origin.equals(flavor)) {
//            return new ResponseEntity<>(new Message("findAll", beanRepository.findAll()), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(new Message("findFilter", beanRepository.findByRoastingLevelOrOriginOrFlavor(
//                    roastingLevel, origin, flavor
//            )), HttpStatus.OK);
//        }
//    }

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

    public ResponseEntity<Message> getSearchPage(String keyword, String sort, String [] filter) {
        List<Bean> beanList = beanRepository.findBeanByRequestDto(keyword, sort, filter);
        return new ResponseEntity<>(new Message("Success", beanList), HttpStatus.OK);
    }

    // 상세페이지(페이징 처리)
    public ResponseEntity<Message> getDetailPage(Long cardId, String address, int pageNumber) {
        Bean bean = utilService.checkBean(cardId);
        List<CafeResponseDto> cafeList = setDetailPageCafe(bean, address);
        // Bean에 연결된 Comment 목록을 가져오기
        int commentCount = commentRepository.findByBean_id(bean.getId()).size();
        List<Comment> commentList = setDetailPageComment(bean, pageNumber);
        return new ResponseEntity<>(new Message("Success", new DetailPageResponseDto(commentCount ,bean, cafeList, commentList)), HttpStatus.OK);
    }

    public List<CafeResponseDto> setDetailPageCafe(Bean bean, String address) {
        String [] splitAddress = address.split(" ");
        return cafeRepository.findByBeanAndCafeAddressContaining(bean, splitAddress[1]);
    }

    public List<Comment> setDetailPageComment(Bean bean, int pageNumber) {
        return commentRepository.findByBean(bean, pageNumber);
    }
    //상세페이지(페이징 처리 없이 가져옴)
    public ResponseEntity<Message> getDetailBean(Long cardId, String address) {
        Bean bean = utilService.checkBean(cardId);
        List<CafeResponseDto> cafeList = setDetailPageCafe(bean, address);
        // Bean에 연결된 Comment 목록을 가져오기
        int commentCount = commentRepository.findByBean_id(bean.getId()).size();
        List<Comment> commentList = setDetailPageComment(bean);
        return new ResponseEntity<>(new Message("Success", new DetailPageResponseDto(commentCount,bean, cafeList, commentList)), HttpStatus.OK);
    }
    public List<Comment> setDetailPageComment(Bean bean) {
        return commentRepository.findByBean_id(bean.getId());
    }
}

