package com.cuping.cupingbe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BeanRequestDto {

    private String [] filterList;
    private String sort;
    private String keyword;
    
}
