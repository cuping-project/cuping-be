package com.cuping.cupingbe.dto;

import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AdminPageRequestDto {
    private MultipartFile image;
    private String beanName;
    private String beanSummary;
    private String beanInfo;
    private String roastingLevel;
    private String flavor;
    private String origin;

}
