package com.cuping.cupingbe.service;

import com.cuping.cupingbe.dto.OwnerPageRequestDto;
import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.global.exception.CustomException;
import com.cuping.cupingbe.global.exception.ErrorCode;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.repository.CafeRepository;
import com.cuping.cupingbe.s3.S3Uploader;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.web.multipart.MultipartFile;
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

    //카페 등록 요청
    @Transactional
    public ResponseEntity<Message> createCafe(OwnerPageRequestDto ownerPageRequestDto) throws Exception {
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
            String imgUrl = s3Uploader.upload(ownerPageRequestDto.getImage());

                Cafe cafe = Cafe.builder()
                        .cafeAddress(rowNode.path("road_address_name").asText())
                        .cafePhoneNumber(ownerPageRequestDto.getStoreNumber())
                        .cafeName(rowNode.path("place_name").asText())
                        .x(rowNode.path("x").asText())
                        .y(rowNode.path("y").asText())
                        .imageUrl(imgUrl)
                        .build();

                cafeRepository.save(cafe);

        }


        return new ResponseEntity<>(new Message("가게 등록 성공"), HttpStatus.OK);
    }
}
