package com.cuping.cupingbe.entity;

import com.cuping.cupingbe.dto.AdminPageRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

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

    public Bean(String imgUrl, AdminPageRequestDto adminPageRequestDto) {
        this.beanName = adminPageRequestDto.getBeanName();
        this.beanImage = imgUrl;
        this.beanSummary = adminPageRequestDto.getBeanSummary();
        this.beanInfo = adminPageRequestDto.getBeanInfo();
        this.roastingLevel = adminPageRequestDto.getRoastingLevel();
        this.flavor = adminPageRequestDto.getFlavor();
        this.origin = adminPageRequestDto.getOrigin();
    }
}
