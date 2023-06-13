package com.cuping.cupingbe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class AdminPageRequestDto {
    private MultipartFile image;
    private String beanName;
    private String beanSummary;
    private String beanInfo;
    private String roastingLevel;
    private String flavor;
    private String origin;
    private String hashTag;
}
