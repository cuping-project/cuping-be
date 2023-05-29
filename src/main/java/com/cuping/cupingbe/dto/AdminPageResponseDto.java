package com.cuping.cupingbe.dto;

import com.cuping.cupingbe.entity.Cafe;

import lombok.Getter;

@Getter
public class AdminPageResponseDto {
    private Long cafeId;
    private String cafeAddress;
    private String cafePhoneNumber;
    private String cafeName;
    private String x;
    private String y;
    private Boolean permit;
    public AdminPageResponseDto(Cafe cafe) {
        this.cafeId = cafe.getId();
        this.cafeAddress = cafe.getCafeAddress();
        this.cafeName = cafe.getCafeName();
        this.cafePhoneNumber = cafe.getCafePhoneNumber();
        this.x = cafe.getX();
        this.y = cafe.getY();
        this.permit = cafe.getPermit();
    }
}
