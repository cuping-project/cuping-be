package com.cuping.cupingbe.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Cafe {

    @Id // 카카오맵에서 제공하는 ID를 사용하려고 GeneratedValue추가 안함.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cafeId;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @Column
    private String cafeAddress;

    @Column
    private String storePhoneNumber;

    @Column
    private String cafeName;

    @ManyToOne
    @JoinColumn(name = "bean_id")
    private Bean bean;

    @Column
    private String x;

    @Column
    private String y;

    @Column
    private Boolean permit;

    @Column
    private String imageUrl;

    @Builder
    public Cafe(Long cafeId, String cafeAddress, String cafePhoneNumber, String cafeName, String x, String y, String imageUrl) {
        this.cafeId = cafeId;
        this.cafeAddress = cafeAddress;
        this.storePhoneNumber = cafePhoneNumber;
        this.cafeName = cafeName;
        this.x = x;
        this.y = y;
        this.imageUrl = imageUrl;
    }

    public void setPermit(boolean b) {
        this.permit = b;
    }


}
