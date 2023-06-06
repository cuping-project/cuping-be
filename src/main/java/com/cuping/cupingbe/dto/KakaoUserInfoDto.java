package com.cuping.cupingbe.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {

	private Long id;
	private String email;
	private String nickname;
	private String profile_image;

	public KakaoUserInfoDto(Long id, String nickname, String email, String profile_image) {
		this.id = id;
		this.nickname = nickname;
		this.email = email;
		this.profile_image = profile_image;
	}

	public KakaoUserInfoDto(JsonNode jsonNode) {
		this.id = jsonNode.get("id").asLong();
		this.nickname = jsonNode.get("properties").get("nickname").asText();
		this.email = jsonNode.get("kakao_account").get("email").asText();
		this.profile_image = jsonNode.get("properties").get("profile_image").asText();
	}

}
