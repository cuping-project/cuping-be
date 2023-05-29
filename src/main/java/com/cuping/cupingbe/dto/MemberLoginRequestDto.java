package com.cuping.cupingbe.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberLoginRequestDto {
	private String userId;
	private String password;
	private String ownerId;
}
