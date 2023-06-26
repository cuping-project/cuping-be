package com.cuping.cupingbe.dto;

import com.cuping.cupingbe.entity.Bean;
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
    private List<Bean> heartList;

    public MyPageDto(User user, List<Bean> heartList) {
        this.userId = user.getUserId();
        this.nickname = user.getNickname();
        this.heartList = heartList;
    }
}