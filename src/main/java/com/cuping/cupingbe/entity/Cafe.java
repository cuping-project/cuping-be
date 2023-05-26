package com.cuping.cupingbe.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Entity
@NoArgsConstructor
public class Cafe {

    @Id
    private Long cafeId;

    @Column
    private String addressName;

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

    @Builder
    public Cafe(Long cafeId, String addressName, String storePhoneNumber, String cafeName, String x, String y) {
        this.cafeId = cafeId;
        this.addressName = addressName;
        this.storePhoneNumber = storePhoneNumber;
        this.cafeName = cafeName;
        this.x = x;
        this.y = y;
    }

}
