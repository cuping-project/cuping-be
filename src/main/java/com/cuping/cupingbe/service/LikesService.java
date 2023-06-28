package com.cuping.cupingbe.service;

import com.cuping.cupingbe.dto.LikesResponseDto;
import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Likes;
import com.cuping.cupingbe.entity.User;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.repository.LikesRepository;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;
    private final UtilService utilService;

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public ResponseEntity<Message> toggleLikeStatus(Long beanId, String userId) {
        Bean bean = utilService.checkBean(beanId);
        User user = utilService.checkUserId(userId);

        //좋아요 존재여부 확인
        Likes like = getLikes(user, bean);
        if (like == null) {
            Likes newLikes = likesRepository.save(Likes.addLike(user, bean));
            newLikes.setLikeStatus();
            bean.updateLike(true);
            return new ResponseEntity<>(new Message("좋아요 성공.", new LikesResponseDto(bean, newLikes)), HttpStatus.OK);
        } else {
            like.setLikeStatus();
            bean.updateLike(like.isLikeStatus());
            return new ResponseEntity<>(new Message("좋아요 성공.", new LikesResponseDto(bean, like)), HttpStatus.OK);
        }
    }

    public Likes getLikes(User user, Bean bean) {
        return likesRepository.findByUserAndBean(user, bean).orElse(null);
    }
}
