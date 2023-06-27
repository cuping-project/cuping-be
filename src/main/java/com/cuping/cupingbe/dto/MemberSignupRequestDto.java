package com.cuping.cupingbe.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
@NoArgsConstructor
public class MemberSignupRequestDto {

	@Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d_]{5,12}",
			message = "아이디는 알파벳 대소문자, 숫자를 입력하고 5~12자리로 구성해주세요.")
	private String userId;

	@Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[~!?_@#$%^&*()+|=])[a-z\\d~!?_@#$%^&*()+|=]{8,16}$",
			message = "비밀번호는 알파벳 소문자, 숫자, 특수문자(!?_)를 입력하고 8~12자리로 구성해주세요.")
	private String password;

	private String adminKey;

	private String nickname;

	private String storeName;

	private String storeAddress;

	private String storeNumber;

	private MultipartFile authImage;

	private MultipartFile cafeImage;
}
