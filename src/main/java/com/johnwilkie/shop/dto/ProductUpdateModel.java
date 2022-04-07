package com.johnwilkie.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductUpdateModel {
	
	private String prodid;
	private String description;
	private String name;
	private float discount;
	

}
