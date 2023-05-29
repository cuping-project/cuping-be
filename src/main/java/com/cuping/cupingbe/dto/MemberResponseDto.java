package com.cuping.cupingbe.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberResponseDto {
	private String message;

	public MemberResponseDto(String message){
		this.message =message;
	}
}
