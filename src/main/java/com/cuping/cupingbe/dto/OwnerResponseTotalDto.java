package com.cuping.cupingbe.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OwnerResponseTotalDto {
    List<OwnerResponseDto2> ownerResponseDto2List;
    List<OwnerResponseDto> ownerResponseDtoList;

    public OwnerResponseTotalDto (List<OwnerResponseDto> ownerResponseDto, List<OwnerResponseDto2> ownerResponseDto2) {
        this.ownerResponseDtoList = ownerResponseDto;
        this.ownerResponseDto2List = ownerResponseDto2;
    }
}
