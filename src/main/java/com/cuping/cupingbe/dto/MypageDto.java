package com.cuping.cupingbe.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class MypageDto {
    private String userId;
    private String nickname;
    private String password;
    private List<Long> heartList;


}