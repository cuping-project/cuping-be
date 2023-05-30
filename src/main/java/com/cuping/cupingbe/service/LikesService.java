package com.cuping.cupingbe.service;

import com.cuping.cupingbe.dto.LikesResponseDto;
import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Likes;
import com.cuping.cupingbe.entity.User;
import com.cuping.cupingbe.repository.BeanRepository;
import com.cuping.cupingbe.repository.LikesRepository;
import com.cuping.cupingbe.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final BeanRepository beanRepository;


    @Transactional
    public ResponseEntity<LikesResponseDto> toggleLikeStatus(Long beanId, String userId) {
        Bean bean = beanRepository.findById(beanId)
                .orElseThrow(() -> new IllegalArgumentException("원두를 찾을 수 없습니다."));
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        //좋아요 존재여부 확인
        Likes like = likesRepository.findByUserAndBean(user, bean);
        if (like == null) {
            Likes newLikes = likesRepository.save(Likes.addLike(user, bean));
            newLikes.setLikeStatus();
            bean.updateLike(true);
            return new ResponseEntity<>(new LikesResponseDto(bean, newLikes), HttpStatus.OK);
        } else {
            if (!like.isLikeStatus()) {
                like.setLikeStatus();
                bean.updateLike(true);
            } else {
                like.setLikeStatus();
                bean.updateLike(false);
            }
            return new ResponseEntity<>(new LikesResponseDto(bean, like), HttpStatus.OK);
        }
    }
}
