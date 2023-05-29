package com.cuping.cupingbe.dto;

import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Cafe;
import lombok.Getter;

import java.util.List;

@Getter
public class DetailPageResponseDto {

	private final Bean bean;
	private final List<Cafe> cafeList;

	public DetailPageResponseDto(Bean bean, List<Cafe> cafeList) {
		this.bean = bean;
		this.cafeList = cafeList;
	}
}
