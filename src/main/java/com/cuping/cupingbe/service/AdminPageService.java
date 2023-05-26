package com.cuping.cupingbe.service;

import com.cuping.cupingbe.dto.AdminPageRequestDto;
import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.repository.BeanRepository;
import com.cuping.cupingbe.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AdminPageService {

    private final S3Uploader s3Uploader;
    private final BeanRepository beanRepository;
    public ResponseEntity<Message> createBean(AdminPageRequestDto adminPageRequestDto) throws IOException {
        String imgUrl = s3Uploader.upload(adminPageRequestDto.getImage());
        Bean bean = new Bean(imgUrl, adminPageRequestDto);
        beanRepository.save(bean);
        Message message = new Message("가게 등록 성공");
        ResponseEntity<Message> responseEntity = new ResponseEntity<>(message, HttpStatus.OK);
        return responseEntity;
    }



}
