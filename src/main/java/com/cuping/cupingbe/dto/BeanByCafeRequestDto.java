package com.cuping.cupingbe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BeanByCafeRequestDto {

    private String cafeAddress;
    private String beanName;
    private String beanRoastingLevel;
    private String beanOrigin;
}
