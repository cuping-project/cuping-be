package com.cuping.cupingbe.service;

import com.cuping.cupingbe.dto.AddBeanByCafeRequestDto;
import com.cuping.cupingbe.dto.OwnerPageRequestDto;
import com.cuping.cupingbe.dto.OwnerResponseDto;
import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.entity.User;
import com.cuping.cupingbe.entity.UserRoleEnum;
import com.cuping.cupingbe.global.exception.CustomException;
import com.cuping.cupingbe.global.exception.ErrorCode;
import com.cuping.cupingbe.global.security.UserDetailsImpl;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.repository.BeanRepository;
import com.cuping.cupingbe.repository.CafeRepository;
import com.cuping.cupingbe.repository.UserRepository;
import com.cuping.cupingbe.s3.S3Uploader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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

        //사장 권한이 있는지 확인
        UserRoleEnum userRoleEnum = user.getRole();
        System.out.println("role = " + userRoleEnum);
        if (userRoleEnum != UserRoleEnum.OWNER) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_OWNER);
        } else {
            userRepository.findByUserId(user.getUserId()).orElseThrow(
                    () -> new CustomException(ErrorCode.USER_NOT_FOUND)
            );
            String query = ownerPageRequestDto.getStoreAddress() + ownerPageRequestDto.getStoreName();
            byte[] bytes = query.getBytes(StandardCharsets.UTF_8);
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            String encode = StandardCharsets.UTF_8.decode(buffer).toString();

            URI uri = UriComponentsBuilder
                    .fromUriString("https://dapi.kakao.com")
                    .path("/v2/local/search/keyword.json")
                    .queryParam("query", encode)
                    .queryParam("size", 15)
                    .encode()
                    .build()
                    .toUri();

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + "49244bfa76b071b6bad74b2441b968d7");
            RequestEntity<Void> req = RequestEntity
                    .get(uri)
                    .headers(headers)
                    .build();
            ResponseEntity<String> result = restTemplate.exchange(req, String.class);
            String jsonResponse = result.getBody();

            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            JsonNode documents = jsonNode.path("documents");

            for (JsonNode rowNode : documents) { // 데이터를 가져와서 Cafe 엔티티로 매핑하는 작업
                long cafeId = rowNode.path("id").asLong();
                if (cafeRepository.findById(cafeId).isPresent()) {
                    throw new CustomException(ErrorCode.DUPLICATE_CAFE);
                }
                //사업자 등록증 SC저장
                String imgUrl = s3Uploader.upload(ownerPageRequestDto.getAuthImage());

                Cafe cafe = Cafe.builder()
                        .owner(user)
                        .cafeAddress(ownerPageRequestDto.getStoreAddress())
                        .cafePhoneNumber(ownerPageRequestDto.getStoreNumber())
                        .cafeName(ownerPageRequestDto.getStoreName())
                        .x(rowNode.path("x").asText())
                        .y(rowNode.path("y").asText())
                        .imageUrl(imgUrl)
                        .build();

                cafeRepository.save(cafe);

            }


            return new ResponseEntity<>(new Message("가게 등록 성공"), HttpStatus.OK);
        }
    }

    //카페 삭제
    public ResponseEntity<Message> deleteCafe(Long cafeId, UserDetailsImpl userDetails) throws Exception {

        //사용자 권환 확인 (ADMUN인지 아닌지)
        UserRoleEnum userRoleEnum2 = userDetails.getUser().getRole();
        System.out.println("role = " + userRoleEnum2);
        if (userRoleEnum2 == UserRoleEnum.ADMIN) {
            cafeRepository.findById(cafeId).orElseThrow(
                    () -> new Exception(ErrorCode.UNREGISTER_CAFE.getDetail())
            );
            cafeRepository.deleteById(cafeId);
            return new ResponseEntity<>(new Message("카페가 삭제 되었습니다."), HttpStatus.OK);
        }
        //사장 권한이 있는지 확인
        UserRoleEnum userRoleEnum = userDetails.getUser().getRole();
        System.out.println("role = " + userRoleEnum);
        if (userRoleEnum != UserRoleEnum.OWNER) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_OWNER);
        } else {
            cafeRepository.findById(cafeId).orElseThrow(
                    () -> new Exception(ErrorCode.UNREGISTER_CAFE.getDetail())
            );
        Cafe cafe = cafeRepository.findByOwnerId(userDetails.getUser().getId());
            if (cafe == null) {
                    throw  new Exception(ErrorCode.USER_NOT_FOUND.getDetail());
            }
            cafeRepository.deleteById(cafeId);
            return new ResponseEntity<>(new Message("카페가 삭제 되었습니다."), HttpStatus.OK);
        }
    }

    //사장페이지 카페 조회
    public List<OwnerResponseDto> getCafe(UserDetailsImpl userDetails) throws Exception {
        List<Cafe> cafeList = cafeRepository.findAllByOwnerId(userDetails.getUser().getId());
        if(cafeList == null) {
            throw new Exception(ErrorCode.UNREGISTER_CAFE.getDetail());
        }
        List<OwnerResponseDto> ownerResponseDtoList = new ArrayList();
        for(Cafe cafe : cafeList){
            if(cafe.getPermit() == true) {
                ownerResponseDtoList.add(new OwnerResponseDto(cafe));
            }
        }
        return ownerResponseDtoList;
    }

    //(사장페이지) 카페에 원두 등록
    public ResponseEntity<Message> addBeanByCafe(AddBeanByCafeRequestDto addBeanByCafeRequestDto, UserDetailsImpl userDetails) {
        UserRoleEnum userRoleEnum = userDetails.getUser().getRole();
        System.out.println("role = " + userRoleEnum);
        if (userRoleEnum != UserRoleEnum.OWNER) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_OWNER);
        } else {
            Cafe cafe = cafeRepository.findByOwnerId(userDetails.getUser().getId());
            Bean bean =  beanRepository.findBybeanName(addBeanByCafeRequestDto.getBeanName());
            cafe.setBean(bean);
            return new ResponseEntity<>(new Message("카페에 원두가 등록 되었습니다."), HttpStatus.OK);
        }
    }


}
