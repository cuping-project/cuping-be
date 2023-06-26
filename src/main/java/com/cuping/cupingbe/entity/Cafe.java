package com.cuping.cupingbe.entity;

import com.cuping.cupingbe.dto.OwnerPageRequestDto;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.Nullable;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Cafe {

    @Id
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
    private String businessImage;

    @Column
    private String cafeImage;

    @Column
    private String city;

    @Column
    private String district;

    @Column
    private String detailLink;

    @Column
    private String homePageLink;

    @Column
    private String openDay;

    @Column
    private String openTime;

    public Cafe(User owner, Cafe cafe, Bean bean) {
        this.owner = owner;
        this.cafeAddress = cafe.getCafeAddress();
        String [] split = cafe.getCafeAddress().split(" ");
        this.city = split[0];
        this.district = split[1];
        this.cafePhoneNumber = cafe.getCafePhoneNumber();
        this.cafeName = cafe.getCafeName();
        this.x = cafe.getX();
        this.y = cafe.getY();
        this.businessImage = cafe.getBusinessImage();
        this.permit = cafe.getPermit();
        this.bean = bean;
        this.cafeImage = cafe.getCafeImage();
    }

    public Cafe(User user, OwnerPageRequestDto ownerPageRequestDto, JsonNode documents, String businessImage, String cafeImage) {
        this.owner = user;
        this.cafeAddress = ownerPageRequestDto.getStoreAddress();
        String [] split = ownerPageRequestDto.getStoreAddress().split(" ");
        this.city = split[0];
        this.district = split[1];
        this.cafePhoneNumber = ownerPageRequestDto.getStoreNumber();
        this.cafeName = ownerPageRequestDto.getStoreName();
        this.x = documents.get(0).path("x").asText();
        this.y = documents.get(0).path("y").asText();
        this.businessImage = businessImage;
        this.cafeImage = cafeImage;
    }

    public Cafe setPermit(boolean b) {
        this.permit = b;
        return this;
    }
    
    public void setBean(Bean bean) {
        this.bean = bean;
    }
}