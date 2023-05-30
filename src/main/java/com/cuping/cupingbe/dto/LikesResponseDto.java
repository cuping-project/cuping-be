package com.cuping.cupingbe.dto;

import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Likes;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LikesResponseDto {
    private int likeCount;
    private boolean likeStatus;

    public LikesResponseDto(Bean bean, Likes likes) {
        this.likeCount = bean.getLikesCount();
        this.likeStatus = likes.isLikeStatus();
    }
}
