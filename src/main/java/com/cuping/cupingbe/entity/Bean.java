package com.cuping.cupingbe.entity;

import com.cuping.cupingbe.dto.AdminPageRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column
    private String hashTag;

    @Column
    private String beanInfo;

    @Column
    private String roastingLevel;

    @Column
    private String flavor;

    @Column
    private String origin;
    // 좋아요 개수를 관리할 필드 추가
    @Column
    private int likesCount = 0;

    public Bean(String imgUrl, AdminPageRequestDto adminPageRequestDto) {
        this.beanName = adminPageRequestDto.getBeanName();
        this.beanImage = imgUrl;
        this.beanSummary = adminPageRequestDto.getBeanSummary();
        this.beanInfo = adminPageRequestDto.getBeanInfo();
        this.roastingLevel = adminPageRequestDto.getRoastingLevel();
        this.flavor = adminPageRequestDto.getFlavor();
        this.origin = adminPageRequestDto.getOrigin();
        this.hashTag = adminPageRequestDto.getHashTag();
        this.beanOriginName = this.beanName + this.origin;
    }

    // 좋아요 개수를 증가시키는 메서드
    public void updateLike(Boolean likeOrDislike) {
        this.likesCount = Boolean.TRUE.equals(likeOrDislike) ? this.likesCount + 1 : this.likesCount - 1;
    }
}
