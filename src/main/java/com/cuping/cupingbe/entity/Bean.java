package com.cuping.cupingbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Bean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany
    private List<Cafe> cafeList;
    private String beanName;
    private String beanImageUrl; // binarycode
    private String beanSummary;
    private String beanInfo;
    private String hastTag;
    private String roastingLevel;
    private String flavor;
    private String origin;

    public void setCafeList(List<Cafe> cafeList) {
        this.cafeList = cafeList;
    }
}
