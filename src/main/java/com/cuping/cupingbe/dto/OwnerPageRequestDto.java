package com.cuping.cupingbe.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class OwnerPageRequestDto {

    private MultipartFile image;
    private String storeName;
    private String storeAddress;
    private String storeNumber;
    private String authImage;

}
