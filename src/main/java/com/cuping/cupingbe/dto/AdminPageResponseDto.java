package com.cuping.cupingbe.dto;

import com.cuping.cupingbe.entity.Cafe;

import lombok.Getter;

@Getter
public class AdminPageResponseDto {
    private Long cafeId;
    private String cafeAddress;
    private String storePhoneNumber;
    private String cafeName;
//    @Column
//    private List<bean> beanList = ArrayList;
    private String x;
    private String y;
    private Boolean permit;
    public AdminPageResponseDto(Cafe cafe) {
        this.cafeId = cafe.getCafeId();
        this.cafeAddress = cafe.getCafeAddress();
        this.cafeName = cafe.getCafeName();
        this.storePhoneNumber = cafe.getStorePhoneNumber();
        this.x = cafe.getX();
        this.y = cafe.getY();
        this.permit = cafe.getPermit();
    }
}
