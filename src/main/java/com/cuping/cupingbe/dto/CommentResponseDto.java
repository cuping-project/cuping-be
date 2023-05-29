package com.cuping.cupingbe.dto;

import com.cuping.cupingbe.entity.Comment;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommentResponseDto {

    private String content;
    private Long id;

    public CommentResponseDto(Comment comment) {

        this.content = comment.getContent();
        this.id = comment.getId();

    }
}
