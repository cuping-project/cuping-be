package com.cuping.cupingbe.dto;

import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class OwnerResponseDto {
    private String cafeAddress;
    private String cafePhoneNumber;
    private String cafeName;
    private String x;
    private String y;
    private Boolean permit;
    private List<Bean> beans;
    public OwnerResponseDto(Cafe cafe) {
        this.cafeAddress = cafe.getCafeAddress();
        this.cafePhoneNumber = cafe.getCafePhoneNumber();
        this.cafeName = cafe.getCafeName();
        this.x = cafe.getX();
        this.y = cafe.getY();
        this.permit = cafe.getPermit();
    }

    public List<Bean> getBeans() {
        if (beans == null) {
            beans = new ArrayList<>();
        }
        return beans;
    }
}
