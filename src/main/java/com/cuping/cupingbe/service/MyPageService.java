package com.cuping.cupingbe.service;

import com.cuping.cupingbe.dto.MyPageDto;
import com.cuping.cupingbe.dto.UserUpdateRequestDto;
import com.cuping.cupingbe.entity.Likes;
import com.cuping.cupingbe.entity.User;
import com.cuping.cupingbe.global.exception.CustomException;
import com.cuping.cupingbe.global.exception.ErrorCode;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.repository.LikesRepository;
import com.cuping.cupingbe.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MyPageService {

    private UserRepository userRepository;
    private LikesRepository likesRepository;
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<Message> getUserById(User user) {

        // 유저가 누른 좋아요를 갖고옴
        List<Likes> likes = likesRepository.findAllByUser(user);
        // 좋아요 목록을 생성
        List<Long> heartList = likes.stream()
                .filter(Likes::isLikeStatus)
                .map(like -> like.getBean().getId())
                .collect(Collectors.toList());
        MyPageDto myPageDto = new MyPageDto(user, heartList);
        return new ResponseEntity<>(new Message("마이페이지.", myPageDto), HttpStatus.OK);
    }

    @Transactional
    public void updateUser(User user, UserUpdateRequestDto requestDto) {

        // 현재 비밀번호 확인
        String currentPassword = requestDto.getCurrentPassword();
        if (currentPassword == null || !passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // 닉네임 업데이트. 클라이언트가 닉네임을 안보냈으면 기존 닉네임 유지
        String newNickname = requestDto.getNickname();
        if (newNickname != null && !newNickname.isEmpty()) {
            user.setNickname(newNickname);
        }

        // 비밀번호 업데이트. 클라이언트에서 비밀번호가 안오면 기존 비밀번호 유지
        String newPassword = requestDto.getNewPassword();
        if (newPassword != null && !newPassword.isEmpty()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }
    }
}