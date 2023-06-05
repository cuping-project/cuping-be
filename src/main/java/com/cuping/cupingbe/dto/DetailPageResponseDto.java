package com.cuping.cupingbe.dto;

import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.entity.Comment;
import lombok.Getter;

import java.util.List;

@Getter
public class DetailPageResponseDto {

    private final Bean bean;
    private final List<Cafe> cafeList;
    private final List<Comment> commentList;

    public DetailPageResponseDto(Bean bean, List<Cafe> cafeList, List<Comment> commentList) {
        this.bean = bean;
        this.cafeList = cafeList;
        this.commentList = commentList;
    }
}
