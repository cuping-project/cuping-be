package com.cuping.cupingbe.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CommentDeleteRequestDto {
    private Long id;
    private String userId;
    private Long cardId; // 게시글 아이디
}
