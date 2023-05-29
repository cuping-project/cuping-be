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

    @ManyToOne //(fetch = FetchType.EAGER) // 사용자 사장님 컬럼 받아서 수정 해야함
    @JoinColumn(name="User_id", nullable = false)
    private User user;


    @ManyToOne //(fetch = FetchType.EAGER) // 사용자 사장님 컬럼 받아서 수정 해야함
    @JoinColumn(name="User_id", nullable = false)
    private Owner owner;





    public Comment(CommentRequestDto commentRequestDto, User user, String username) { //댓글 작성
        this.content = commentRequestDto.getContent();
        this.username = username;
        this.user = user;
    }



    public void update(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }







}