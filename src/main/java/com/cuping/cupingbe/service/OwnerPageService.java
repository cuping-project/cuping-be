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
import com.cuping.cupingbe.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final BeanRepository beanRepository;

    //카페 등록 요청
    @Transactional
    public ResponseEntity<Message> createCafe(OwnerPageRequestDto ownerPageRequestDto, User user) throws Exception {

        checkCreateCafe(user, ownerPageRequestDto.getStoreAddress());
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

        JsonNode documents = objectMapper.readTree(new RestTemplate()
                .exchange(req, String.class).getBody()).path("documents");

        //사업자 등록증 SC저장
        String imgUrl = s3Uploader.upload(ownerPageRequestDto.getAuthImage());
        String x = documents.get(0).path("x").asText();
        String y = documents.get(0).path("y").asText();
        cafeRepository.save(new Cafe(user, ownerPageRequestDto, x, y, imgUrl));

        return new ResponseEntity<>(new Message("가게 등록 성공"), HttpStatus.OK);
    }

    public void checkCreateCafe(User user, String storeAddress) {
        userRepository.findByUserId(user.getUserId()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        if (user.getRole() != UserRoleEnum.OWNER)
            throw new CustomException(ErrorCode.UNAUTHORIZED_OWNER);
        if (cafeRepository.findByCafeAddress(storeAddress).isPresent())
            throw new CustomException(ErrorCode.DUPLICATE_CAFE);
    }

    //카페 삭제
    @Transactional
    public ResponseEntity<Message> deleteCafe(Long cafeId, User user) throws Exception {

        UserRoleEnum role = user.getRole();
        if (role.equals(UserRoleEnum.OWNER) || role.equals(UserRoleEnum.ADMIN)) {
            userRepository.findByUserId(user.getUserId()).orElseThrow(() ->
                    new CustomException(ErrorCode.USER_NOT_FOUND));
            cafeRepository.findById(cafeId).orElseThrow(() ->
                    new CustomException(ErrorCode.UNREGISTER_CAFE));
            if (role.equals(UserRoleEnum.OWNER)) {
                cafeRepository.findByOwnerIdAndId(user.getId(), cafeId).orElseThrow(() ->
                        new CustomException(ErrorCode.UNAUTHORIZED_OWNER));
            }
            cafeRepository.deleteById(cafeId);
        }
        return new ResponseEntity<>(new Message("카페가 삭제 되었습니다."), HttpStatus.OK);
    }

    //사장페이지 카페 조회
    public ResponseEntity<Message> getCafe(User user) {

        if (user.getRole() != UserRoleEnum.OWNER) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_OWNER);
        }
        List<Cafe> cafeList = cafeRepository.findAllByOwnerId(user.getId());
        if (cafeList == null) {
            throw new CustomException(ErrorCode.CAFE_NOT_FOUND);
        }

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
        return new ResponseEntity<>(new Message("사장 카페 조회.", cafeMap), HttpStatus.OK);
    }

    //(사장페이지) 카페에 원두 등록
    public ResponseEntity<Message> addBeanByCafe(BeanByCafeRequestDto requestDto, User user) {

        if (!user.getRole().equals(UserRoleEnum.OWNER)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_OWNER);
        }
        Bean bean = beanRepository.findByBeanOriginNameAndRoastingLevel(
                requestDto.getBeanOrigin() + requestDto.getBeanName(), requestDto.getBeanRoastingLevel()).orElseThrow(() ->
                new CustomException(ErrorCode.UNREGISTER_BEAN));

        Cafe cafe = cafeRepository.findFirstByCafeAddressAndOwnerId(requestDto.getCafeAddress(), user.getId()).orElseThrow(() ->
                new CustomException(ErrorCode.UNREGISTER_CAFE));

        cafeRepository.save(new Cafe(user, cafe, bean));
            return new ResponseEntity<>(new Message("카페에 원두가 등록되었습니다.", null), HttpStatus.OK);
        }

    //(사장페이지) 카페에 등록된 원두 삭제
    public ResponseEntity<Message> deleteBeanByCafe(BeanByCafeRequestDto requestDto, User user) {

        if (!user.getRole().equals(UserRoleEnum.OWNER)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_OWNER);
        }
        Bean bean = beanRepository.findByBeanOriginNameAndRoastingLevel(
                requestDto.getBeanOrigin() + requestDto.getBeanName(), requestDto.getBeanRoastingLevel()).orElseThrow(() ->
                new CustomException(ErrorCode.UNREGISTER_BEAN));

        Cafe cafe = cafeRepository.findByCafeAddressAndBeanIdAndOwnerId(requestDto.getCafeAddress(), bean.getId(), user.getId()).orElseThrow(() ->
                new CustomException(ErrorCode.UNREGISTER_CAFE));

        cafeRepository.delete(cafe);
        return new ResponseEntity<>(new Message("원두 삭제 성공", null), HttpStatus.OK);
    }
}
