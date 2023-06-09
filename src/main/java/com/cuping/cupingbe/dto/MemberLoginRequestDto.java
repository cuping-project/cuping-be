package com.cuping.cupingbe.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberLoginRequestDto {
	@NotBlank(message = "아이디를 입력해 주세요")
	private String userId;
	@NotBlank(message = "비밀번호를 입력해 주세요")
	private String password;
	@NotBlank(message = "아이디를 입력해 주세요")
	private String ownerId;
}
