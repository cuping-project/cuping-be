package com.cuping.cupingbe.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Entity
@Getter
@NoArgsConstructor
public class Cafe {

    @Id // 카카오맵에서 제공하는 ID를 사용하려고 GeneratedValue추가 안함.
    private Long cafeId;
  
    @ManyToOne
    private Owner ownerId;

    @Column
    private String cafeAddress;

    @Column
    private String storePhoneNumber;

    @Column
    private String cafeName;

//    @Column
//    private List<bean> beanList = ArrayList;

    @Column
    private String x;

    @Column
    private String y;

    @Column
    private Boolean permit = false;

    @Column
    private String imageUrl;

    @Builder
    public Cafe(Long cafeId, String addressName, String storePhoneNumber, String cafeName, String x, String y, String imageUrl) {
        this.cafeId = cafeId;
        this.cafeAddress = addressName;
        this.storePhoneNumber = storePhoneNumber;
        this.cafeName = cafeName;
        this.x = x;
        this.y = y;
        this.imageUrl = imageUrl;
    }

    public void setPermit(boolean b) {
        this.permit = b;
    }


}
