package com.cuping.cupingbe.dto;

import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
public class OwnerSignupRequestDto {

    private String userId;
    private String password;
    private String nickname;
    private String storeName;
    private String storeAddress;
    private String storeNumber;
    private MultipartFile authImage;

}
