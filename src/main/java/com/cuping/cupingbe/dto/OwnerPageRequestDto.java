package com.cuping.cupingbe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OwnerPageRequestDto {

    private String storeName;
    private String storeAddress;
    private String storeNumber;
    private MultipartFile authImage;
    private MultipartFile cafeImage;

    public OwnerPageRequestDto(String storeName, String storeAddress, String storeNumber, MultipartFile authImage) {
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeNumber = storeNumber;
        this.authImage = authImage;
    }
}
