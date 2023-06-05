package com.cuping.cupingbe.dto;

import com.cuping.cupingbe.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class MyPageDto {
    private String userId;
    private String nickname;
    private String password;
    private List<Long> heartList;

    public MyPageDto(User user, List<Long> heartList) {
        this.userId = user.getUserId();
        this.nickname = user.getNickname();
        this.password = user.getPassword();
        this.heartList = heartList;
    }
}