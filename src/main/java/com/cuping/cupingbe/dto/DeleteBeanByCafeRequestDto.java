package com.cuping.cupingbe.dto;

import lombok.Getter;

@Getter
public class DeleteBeanByCafeRequestDto {
    private String cafeName;
    private String beanName;
    private int beanRoastingLevel;
    private String beanOrigin;

}
