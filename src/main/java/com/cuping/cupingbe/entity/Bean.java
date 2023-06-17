package com.cuping.cupingbe.entity;

import com.cuping.cupingbe.dto.AdminPageRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private String origin;
    // 좋아요 개수를 관리할 필드 추가
    @Column
    private int likesCount = 0;

    @Column
    private boolean sour = false;
    @Column
    private boolean bitter = false;
    @Column
    private boolean burnt = false;
    @Column
    private boolean sweet = false;

    public Bean(String imgUrl, AdminPageRequestDto adminPageRequestDto) {
        this.beanName = adminPageRequestDto.getBeanName();
        this.beanImage = imgUrl;
        this.beanSummary = adminPageRequestDto.getBeanSummary();
        this.beanInfo = adminPageRequestDto.getBeanInfo();
        this.roastingLevel = adminPageRequestDto.getRoastingLevel();
        this.origin = adminPageRequestDto.getOrigin();
        this.sour = adminPageRequestDto.isSour();
        this.bitter = adminPageRequestDto.isBitter();
        this.burnt = adminPageRequestDto.isBurnt();
        this.sweet = adminPageRequestDto.isSweet();
        this.beanOriginName = this.origin + this.beanName;
    }

    // 좋아요 개수를 증가시키는 메서드
    public void updateLike(Boolean likeOrDislike) {
        this.likesCount = Boolean.TRUE.equals(likeOrDislike) ? this.likesCount + 1 : this.likesCount - 1;
    }
}
