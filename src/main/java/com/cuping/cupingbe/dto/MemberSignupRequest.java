package com.cuping.cupingbe.dto;

import com.cuping.cupingbe.entity.UserRoleEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class MemberSignupRequest {

	private String userId;

	private String password;

	private boolean admin = false;

	private String adminKey;

	private String nickname;

	private UserRoleEnum role;

	private String ownerId;


	private String storeName;

	private String storeAddress;

	private String storeNumber;

	private MultipartFile image;





}
