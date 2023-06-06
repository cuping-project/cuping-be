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
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final LikesRepository likesRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    // 마이페이지
    public ResponseEntity<Message> getMyPage(User user) {
        // 유저가 누른 좋아요를 갖고옴
        List<Likes> likesList =setMyPageLikes(user);
        // 좋아요 목록을 생성
        List<Long> heartList = setMyPageHeartList(likesList);
        return new ResponseEntity<>(new Message("마이페이지.", new MyPageDto(user, heartList)), HttpStatus.OK);
    }

    public List<Likes> setMyPageLikes(User user) {
        return likesRepository.findAllByUser(user);
    }

    public List<Long> setMyPageHeartList(List<Likes> likesList) {
        return likesList.stream()
                .filter(Likes::isLikeStatus)
                .map(like -> like.getBean().getId())
                .collect(Collectors.toList());
    }

    // 마이페이지 수정
    @Transactional
    public void updateUser(User user, UserUpdateRequestDto requestDto) {

        // 현재 비밀번호 확인
        checkUserPassword(requestDto.getCurrentPassword(), user.getPassword());

        // 닉네임 업데이트. 클라이언트가 닉네임을 안보냈으면 기존 닉네임 유지
        String newNickname = requestDto.getNickname();
        if (!newNickname.isBlank()) {
            user.setNickname(newNickname);
        }

        // 비밀번호 업데이트. 클라이언트에서 비밀번호가 안오면 기존 비밀번호 유지
        String newPassword = requestDto.getNewPassword();
        if (!newPassword.isBlank()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }
    }

    @Transactional(readOnly = true)
    public User checkUserId(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_ID)
        );
    }

    public void checkUserPassword(String inputPassword, String userPassword) {
        if (inputPassword == null || !passwordEncoder.matches(inputPassword, userPassword))
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
    }
}