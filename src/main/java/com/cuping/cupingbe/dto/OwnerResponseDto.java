package com.cuping.cupingbe.dto;

import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.entity.User;
import jakarta.persistence.*;

public class OwnerResponseDto {


    private Long Id;
    private User owner;
    private Bean bean;
    private String cafeAddress;
    private String cafePhoneNumber;
    private String cafeName;
    private String x;
    private String y;
    private Boolean permit;
    public OwnerResponseDto(Cafe cafe) {
        this.Id = cafe.getId();
        this.owner = cafe.getOwner();
        this.bean = cafe.getBean();
        this.cafeAddress = cafe.getCafeAddress();
        this.cafePhoneNumber = cafe.getCafePhoneNumber();
        this.cafeName = cafe.getCafeName();
        this.x = cafe.getX();
        this.y = cafe.getY();
        this.permit = cafe.getPermit();
    }
}
