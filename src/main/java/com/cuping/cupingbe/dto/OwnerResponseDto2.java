package com.cuping.cupingbe.dto;

import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Cafe;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OwnerResponseDto2 {
    private Long Id;
    private String cafeAddress;
    private String cafePhoneNumber;
    private String cafeName;
    private String x;
    private String y;
    private Boolean permit;
    private Bean bean;

    public OwnerResponseDto2(Cafe cafe, Bean bean) {
        this.Id = cafe.getId();
        this.cafeAddress = cafe.getCafeAddress();
        this.cafePhoneNumber = cafe.getCafePhoneNumber();
        this.cafeName = cafe.getCafeName();
        this.x = cafe.getX();
        this.y = cafe.getY();
        this.permit = cafe.getPermit();
        this.bean = bean;
    }
}
