package com.cuping.cupingbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Likes extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "bean_id")
    private Bean bean;

    @Column(nullable = false)
    private boolean likeStatus = false;

    private Likes(User user, Bean bean) {
        this.user = user;
        this.bean = bean;
    }

    public static Likes addLike(User user, Bean bean) {
        Likes likes = new Likes(user, bean);
        return likes;
    }

    public void setLikeStatus() {
        this.likeStatus = !this.likeStatus;
    }
}