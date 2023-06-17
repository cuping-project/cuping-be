package com.cuping.cupingbe.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private boolean sour;
    @NotNull
    private boolean bitter;
    @NotNull
    private boolean burnt;
    @NotNull
    private boolean sweet;
}
