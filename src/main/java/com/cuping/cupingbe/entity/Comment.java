package com.cuping.cupingbe.entity;


import com.cuping.cupingbe.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @Column
    private String username;

    @ManyToOne
    @JoinColumn(name="User_id", nullable = false)
    private User user;

    public Comment(CommentRequestDto commentRequestDto, User user) {
        this.content = commentRequestDto.getContent();
        this.username = user.getUserId();
        this.user = user;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}