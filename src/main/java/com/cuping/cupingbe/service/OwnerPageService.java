package com.cuping.cupingbe.service;

import com.cuping.cupingbe.dto.*;
import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.entity.User;
import com.cuping.cupingbe.entity.UserRoleEnum;
import com.cuping.cupingbe.global.exception.CustomException;
import com.cuping.cupingbe.global.exception.ErrorCode;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.repository.BeanRepository;
import com.cuping.cupingbe.repository.CafeRepository;
import com.cuping.cupingbe.s3.S3Uploader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OwnerPageService {

    private final CafeRepository cafeRepository;
    private final ObjectMapper objectMapper;
    private final S3Uploader s3Uploader;
    private final BeanRepository beanRepository;
    private final MediatorImpl mediator;

    //카페 등록 요청
    @Transactional
    public ResponseEntity<Message> createCafe(OwnerPageRequestDto ownerPageRequestDto, User user) throws Exception {
        checkCreateCafe(user, ownerPageRequestDto.getStoreAddress());
        JsonNode documents = setCreateCafe(ownerPageRequestDto);
        //사업자 등록증 SC저장
        String imgUrl = s3Uploader.upload(ownerPageRequestDto.getAuthImage());
        cafeRepository.save(new Cafe(user, ownerPageRequestDto, documents, imgUrl));
        return new ResponseEntity<>(new Message("가게 등록 성공", null), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public void checkCreateCafe(User user, String storeAddress) {
        mediator.checkUserId(user.getUserId());
        if (user.getRole() != UserRoleEnum.OWNER)
            throw new CustomException(ErrorCode.FORBIDDEN_OWNER);
        if (cafeRepository.findByCafeAddress(storeAddress).isPresent())
            throw new CustomException(ErrorCode.DUPLICATE_CAFE);
    }

    public JsonNode setCreateCafe(OwnerPageRequestDto ownerPageRequestDto) throws Exception {
        URI uri = UriComponentsBuilder
                .fromUriString("https://dapi.kakao.com")
                .path("/v2/local/search/keyword.json")
                .queryParam("query", ownerPageRequestDto.getStoreAddress())
                .queryParam("size", 1)
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + "49244bfa76b071b6bad74b2441b968d7");
        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .headers(headers)
                .build();

        return objectMapper.readTree(new RestTemplate()
                .exchange(req, String.class).getBody()).path("documents");
    }


    //카페 삭제
    @Transactional
    public ResponseEntity<Message> deleteCafe(Long cafeId, User user) {
        checkDeleteCafe(cafeId, user);
        cafeRepository.deleteById(cafeId);
        return new ResponseEntity<>(new Message("카페가 삭제 되었습니다.", null), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public void checkDeleteCafe(Long cafeId, User user) {
        UserRoleEnum role = user.getRole();
        if (role.equals(UserRoleEnum.OWNER) || role.equals(UserRoleEnum.ADMIN)) {
            mediator.checkUserId(user.getUserId());
            checkCafeId(cafeId);
            if (role.equals(UserRoleEnum.OWNER)) {
                checkOwnerAndCafe(user.getId(), cafeId);
            }
        } else
            throw new CustomException(ErrorCode.FORBIDDEN_OWNER);
    }

    @Transactional(readOnly = true)
    public Cafe checkCafeId(Long cafeId) {
        return cafeRepository.findById(cafeId).orElseThrow(() ->
                new CustomException(ErrorCode.UNREGISTER_CAFE));
    }

    @Transactional(readOnly = true)
    public void checkOwnerAndCafe(Long ownerId, Long cafeId) {
        cafeRepository.findByOwnerIdAndId(ownerId, cafeId).orElseThrow(() ->
                new CustomException(ErrorCode.FORBIDDEN_CAFE));
    }


    //사장페이지 카페 조회
    public ResponseEntity<Message> getCafe(User user) {
        List<Cafe> cafeList = checkGetCafe(user);
        Map<String, OwnerResponseDto> cafeMap = createCafeMap(cafeList);
        return new ResponseEntity<>(new Message("사장 카페 조회.", cafeMap), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public List<Cafe> checkGetCafe(User user) {
        checkRoleOwner(user.getRole());
        List<Cafe> cafeList = cafeRepository.findAllByOwnerId(user.getId());
        if (cafeList == null) {
            throw new CustomException(ErrorCode.CAFE_NOT_FOUND);
        }
        return cafeList;
    }

    public void checkRoleOwner(UserRoleEnum role) {
        if (role.equals(UserRoleEnum.OWNER)) {
            throw new CustomException(ErrorCode.FORBIDDEN_OWNER);
        }
    }

    public Map<String, OwnerResponseDto> createCafeMap(List<Cafe> cafeList) {
        Map<String, OwnerResponseDto> cafeMap = new HashMap<>();
        for (Cafe cafe : cafeList) {
            String cafeAddress = cafe.getCafeAddress();
            OwnerResponseDto ownerResponseDto = cafeMap.get(cafeAddress);
            if (ownerResponseDto == null) {
                ownerResponseDto = new OwnerResponseDto(cafe);
                cafeMap.put(cafeAddress, ownerResponseDto);
            }
            Bean bean = cafe.getBean();
            if (bean != null) {
                ownerResponseDto.getBeans().add(bean);
            }
        }
        return cafeMap;
    }


    //(사장페이지) 카페에 원두 등록
    @Transactional
    public ResponseEntity<Message> addBeanByCafe(User user, BeanByCafeRequestDto requestDto) {
        cafeRepository.save(checkBeanByCafe(user, requestDto, "add"));
            return new ResponseEntity<>(new Message("카페에 원두가 등록되었습니다.", null), HttpStatus.OK);
        }

    //(사장페이지) 카페에 등록된 원두 삭제
    @Transactional
    public ResponseEntity<Message> deleteBeanByCafe(BeanByCafeRequestDto requestDto, User user) {
        cafeRepository.delete(checkBeanByCafe(user, requestDto, "delete"));
        return new ResponseEntity<>(new Message("원두 삭제 성공", null), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public Cafe checkBeanByCafe(User user, BeanByCafeRequestDto requestDto, String type) {
        if (user.getRole().equals(UserRoleEnum.OWNER)) {
            throw new CustomException(ErrorCode.FORBIDDEN_OWNER);
        }
        Bean bean = checkBean(requestDto.getBeanOrigin() + requestDto.getBeanName(),
                requestDto.getBeanRoastingLevel(), false);
        Cafe cafe = type.equals("add") ?
                checkBeanByCafeCafe(requestDto.getCafeAddress(), user.getId()) :
                checkBeanByCafeCafe(requestDto.getCafeAddress(), bean.getId(), user.getId());
        return new Cafe(user, cafe, bean);
    }
    @Transactional(readOnly = true)
    public Bean checkBean(String beanOriginName, String beanRoastingLevel, boolean isDuplicateCheck) {
        Optional<Bean> bean = beanRepository.findByBeanOriginNameAndRoastingLevel(
                beanOriginName, beanRoastingLevel);
        if (isDuplicateCheck) {
            if (bean.isPresent())
                throw new CustomException(ErrorCode.DUPLICATE_BEAN);
            return null;
        } else {
            if (bean.isEmpty())
                throw new CustomException(ErrorCode.UNREGISTER_BEAN);
            return bean.get();
        }
    }
    @Transactional(readOnly = true)
    public Cafe checkBeanByCafeCafe(String cafeAddress, Long userId) {
        return cafeRepository.findFirstByCafeAddressAndOwnerId(cafeAddress, userId).orElseThrow(() ->
                new CustomException(ErrorCode.UNREGISTER_CAFE));
    }
    @Transactional(readOnly = true)
    public Cafe checkBeanByCafeCafe(String cafeAddress, Long beanId, Long userId) {
        return cafeRepository.findByCafeAddressAndBeanIdAndOwnerId(cafeAddress, beanId, userId).orElseThrow(() ->
                new CustomException(ErrorCode.UNREGISTER_CAFE));
    }
}
