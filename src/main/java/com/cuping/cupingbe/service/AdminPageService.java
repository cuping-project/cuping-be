package com.cuping.cupingbe.service;

import com.cuping.cupingbe.dto.AdminPageRequestDto;
import com.cuping.cupingbe.dto.AdminPageResponseDto;
import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.global.exception.ErrorCode;
import com.cuping.cupingbe.global.security.UserDetailsImpl;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.repository.BeanRepository;
import com.cuping.cupingbe.repository.CafeRepository;
import com.cuping.cupingbe.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminPageService {

    private final S3Uploader s3Uploader;
    private final BeanRepository beanRepository;
    private final CafeRepository cafeRepository;

    //(관리자페이지)원두 등록하기
    @Transactional
    public ResponseEntity<Message> createBean(AdminPageRequestDto adminPageRequestDto, UserDetailsImpl userDetails) throws IOException {
        String imgUrl = s3Uploader.upload(adminPageRequestDto.getImage());
        Bean bean = new Bean(imgUrl, adminPageRequestDto);
        beanRepository.save(bean);
        Message message = new Message("가게 등록 성공");
        ResponseEntity<Message> responseEntity = new ResponseEntity<>(message, HttpStatus.OK);
        return responseEntity;
    }

    //(관리자페이지)승인되지 않은 카페 전체 조회
    @Transactional
    public List<AdminPageResponseDto> getPermitCafe(UserDetailsImpl userDetails) {
        List<Cafe> cafeList = cafeRepository.findAllByPermit(false);
        List<AdminPageResponseDto> adminPageResponseDtoList = new ArrayList();
        for(Cafe cafe : cafeList){
            adminPageResponseDtoList.add(new AdminPageResponseDto(cafe));
        }
        return adminPageResponseDtoList;
    }

    //(관리자페이지)카페 승인
    @Transactional
    public ResponseEntity<Message> permitCafe(Long cafeId, UserDetailsImpl userDetails) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(
                () -> new IllegalArgumentException(ErrorCode.UNREGISTER_CAFE.getDetail())
        );
        cafe.setPermit(true);
        cafeRepository.save(cafe);
        Message message = new Message("가게 승인 성공");
        ResponseEntity<Message> responseEntity = new ResponseEntity<>(message, HttpStatus.OK);
        return responseEntity;
    }
}
