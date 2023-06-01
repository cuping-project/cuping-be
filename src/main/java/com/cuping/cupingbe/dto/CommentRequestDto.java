package com.cuping.cupingbe.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class CommentRequestDto {
    private Long id;
    private String content;
    private String userId;
    private Long beanId;
}
