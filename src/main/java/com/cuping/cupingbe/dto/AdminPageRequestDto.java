package com.cuping.cupingbe.dto;

import jakarta.persistence.Column;
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
    private String origin;
    private boolean sour = false;
    private boolean bitter = false;
    private boolean burnt = false;
    private boolean sweet = false;
}
