package com.cuping.cupingbe.service;

import com.cuping.cupingbe.dto.AdminPageRequestDto;
import com.cuping.cupingbe.dto.AdminPageResponseDto;
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
    private final UtilService utilService;

    //(관리자페이지) 원두 등록하기
    @Transactional
    public ResponseEntity<Message> createBean(AdminPageRequestDto adminPageRequestDto, UserDetailsImpl userDetails) throws IOException {
        //관리자 권한, 중복 원두가 있는지 확인.
        utilService.checkRoleAdmin(userDetails.getUser().getRole());
        utilService.checkBean(adminPageRequestDto.getOrigin() + adminPageRequestDto.getBeanName()
                , adminPageRequestDto.getRoastingLevel(), true);

        String imgUrl = s3Uploader.upload(adminPageRequestDto.getImage());
        String imgUrl2 = s3Uploader.upload(adminPageRequestDto.getBeanGraph());
        beanRepository.save(new Bean(imgUrl, imgUrl2, adminPageRequestDto));
        return new ResponseEntity<>(new Message("원두 등록 성공", null), HttpStatus.CREATED);
    }



    //(관리자페이지) 승인되지 않은 카페 전체 조회
    @Transactional
    public ResponseEntity<Message> getPermitCafe(UserDetailsImpl userDetails) {
        //관리자 권한이 있는지 확인
        utilService.checkRoleAdmin(userDetails.getUser().getRole());
        return new ResponseEntity<>(new Message("승인 요청 가게 목록 조회.", createPermitCafeList()), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public List<AdminPageResponseDto> createPermitCafeList() {
        List<Cafe> cafeList = cafeRepository.findAllByPermit(false);
        List<AdminPageResponseDto> adminPageResponseDtoList = new ArrayList<>();
        for (Cafe cafe : cafeList) {
            adminPageResponseDtoList.add(new AdminPageResponseDto(cafe));
        }
        return adminPageResponseDtoList;
    }

    //(관리자페이지) 카페 승인
    @Transactional
    public ResponseEntity<Message> permitCafe(Long cafeId, UserDetailsImpl userDetails) {
        //관리자 권한이 있는지 확인
        utilService.checkRoleAdmin(userDetails.getUser().getRole());
        cafeRepository.save(utilService.checkCafeId(cafeId).setPermit(true));
        return new ResponseEntity<>(new Message("가게 승인 성공", null), HttpStatus.OK);
    }

    // (관리자페이지) 원두 삭제
    @Transactional
    public ResponseEntity<Message> deleteBean(Long beanId, UserDetailsImpl userDetails) {
        //관리자 권한이 있는지 확인
        utilService.checkRoleAdmin(userDetails.getUser().getRole());
        Bean bean = utilService.checkBean(beanId);

        s3Uploader.delete(bean.getBeanImage());
        s3Uploader.delete(bean.getBeanGraph());
        beanRepository.delete(bean);
        return new ResponseEntity<>(new Message("원두 삭제 성공", null), HttpStatus.OK);
    }

    //(관리자 페이지) 원두 전체 조회
    public ResponseEntity<Message> findAllBean(User user) {
        utilService.checkRoleAdmin(user.getRole());
        List<Bean> bean = beanRepository.findAll();
        return new ResponseEntity<>(new Message("사장 카페 조회.", bean), HttpStatus.OK);
    }
}
