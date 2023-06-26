package com.cuping.cupingbe.dto;

import com.cuping.cupingbe.entity.Bean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class CafeResponseDto {

    private Long Id;

    private String cafeAddress;

    private String cafePhoneNumber;

    private String cafeName;

    private String x;

    private String y;

    private String cafeImage;

    private String city;

    private String district;

    private String detailLink;

    private Bean bean;
}
