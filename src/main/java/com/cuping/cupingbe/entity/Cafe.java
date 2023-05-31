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
    private Long Id;
    @ManyToOne
    @JoinColumn(name = "OWNER_ID")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "BEAN_ID")
    private Bean bean;

    @Column
    private String cafeAddress;

    @Column
    private String cafePhoneNumber;

    @Column
    private String cafeName;

    @Column
    private String x;

    @Column
    private String y;

    @Column
    private Boolean permit = false;

    @Column
    private String imageUrl;

    @Builder
    public Cafe (User owner, String cafeAddress, String cafePhoneNumber, String cafeName, String x, String y, String imageUrl) {
        this.owner = owner;
        this.cafeAddress = cafeAddress;
        this.cafePhoneNumber = cafePhoneNumber;
        this.cafeName = cafeName;
        this.x = x;
        this.y = y;
        this.imageUrl = imageUrl;
    }


    public Cafe (User owner, String cafeAddress, String cafePhoneNumber, String cafeName, String x, String y, String imageUrl, Boolean permit,Bean bean) {
        this.owner = owner;
        this.cafeAddress = cafeAddress;
        this.cafePhoneNumber = cafePhoneNumber;
        this.cafeName = cafeName;
        this.x = x;
        this.y = y;
        this.imageUrl = imageUrl;
        this.permit = permit;
        this.bean = bean;
    }

    public void setPermit(boolean b) {
        this.permit = b;
    }


    public void setBean(Bean bean) {
        this.bean = bean;
    }
}