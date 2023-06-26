package com.cuping.cupingbe.dto;

import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.entity.Comment;
import lombok.Getter;

import java.util.List;

@Getter
public class DetailPageResponseDto {

    private final int commentCount;
    private final Bean bean;
    private final List<CafeResponseDto> cafeList;
    private final List<Comment> commentList;

    public DetailPageResponseDto(int commentCount, Bean bean, List<CafeResponseDto> cafeList, List<Comment> commentList) {
        this.commentCount = commentCount;
        this.bean = bean;
        this.cafeList = cafeList;
        this.commentList = commentList;
    }

}
