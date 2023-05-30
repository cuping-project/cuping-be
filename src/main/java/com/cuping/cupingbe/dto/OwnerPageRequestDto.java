package com.cuping.cupingbe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
public class OwnerPageRequestDto {

    private String storeName;
    private String storeAddress;
    private String storeNumber;
    private MultipartFile authImage;
}
