package com.cuping.cupingbe.entity;

import com.cuping.cupingbe.dto.AdminPageRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Bean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column
    private String beanName;

    @Column
    private String beanOriginName;

    @Column
    private String beanImage;

    @Column
    private String beanSummary;

//    @Column
//    private List<hashTag> hashTagList = ArrayList<>();

    @Column
    private String beanInfo;

    @Column
    private String roastingLevel;

    @Column
    private String flavor;

    @Column
    private String origin;

    @Column
    private String hashTag;

//    @OneToMany(mappedBy = "bean")
//    private List<Cafe> cafeList = new ArrayList<>();

//    public void setCafeList(List<Cafe> cafeList) {
//        this.cafeList = cafeList;
//    }

    public Bean(String imgUrl, AdminPageRequestDto adminPageRequestDto) {
        this.beanName = adminPageRequestDto.getBeanName();
        this.beanOriginName = adminPageRequestDto.getOrigin() + adminPageRequestDto.getBeanName();
        this.beanImage = imgUrl;
        this.beanSummary = adminPageRequestDto.getBeanSummary();
        this.beanInfo = adminPageRequestDto.getBeanInfo();
        this.roastingLevel = adminPageRequestDto.getRoastingLevel();
        this.flavor = adminPageRequestDto.getFlavor();
        this.origin = adminPageRequestDto.getOrigin();
        this.hashTag = adminPageRequestDto.getHashTag();
    }
}
