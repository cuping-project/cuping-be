package com.cuping.cupingbe.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDto {
    private String currentPassword;
    private String newPassword;
    private String nickname;
}
