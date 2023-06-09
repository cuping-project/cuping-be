package com.cuping.cupingbe.entity;

import com.cuping.cupingbe.dto.OwnerPageRequestDto;
import com.fasterxml.jackson.databind.JsonNode;
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

    public Cafe(User owner, Cafe cafe, Bean bean) {
        this.owner = owner;
        this.cafeAddress = cafe.getCafeAddress();
        this.cafePhoneNumber = cafe.getCafePhoneNumber();
        this.cafeName = cafe.getCafeName();
        this.x = cafe.getX();
        this.y = cafe.getY();
        this.imageUrl = cafe.getImageUrl();
        this.permit = cafe.getPermit();
        this.bean = bean;
    }
    public Cafe(User user, OwnerPageRequestDto ownerPageRequestDto, JsonNode documents, String imgUrl) {
        this.owner = user;
        this.cafeAddress = ownerPageRequestDto.getStoreAddress();
        this.cafePhoneNumber = ownerPageRequestDto.getStoreNumber();
        this.cafeName = ownerPageRequestDto.getStoreName();
        this.x = documents.get(0).path("x").asText();
        this.y = documents.get(0).path("y").asText();
        this.imageUrl = imgUrl;
    }

    public Cafe setPermit(boolean b) {
        this.permit = b;
        return this;
    }


    public void setBean(Bean bean) {
        this.bean = bean;
    }
}