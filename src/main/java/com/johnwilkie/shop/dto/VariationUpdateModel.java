package com.johnwilkie.shop.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VariationUpdateModel {

	private String varid;
	private BigDecimal price;
	private int stocks;
	
}
