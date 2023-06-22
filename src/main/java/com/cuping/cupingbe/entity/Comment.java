package com.cuping.cupingbe.entity;


import com.cuping.cupingbe.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped{

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

    @ManyToOne
    @JoinColumn(name = "bean_id")
    private Bean bean;

    public Comment(CommentRequestDto commentRequestDto, User user, Bean bean) {
        this.content = commentRequestDto.getContent();
        this.username = user.getUserId();
        this.user = user;
        this.bean = bean;
    }
}