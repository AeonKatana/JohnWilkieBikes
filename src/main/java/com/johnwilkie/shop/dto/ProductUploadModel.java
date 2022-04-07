package com.johnwilkie.shop.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductUploadModel {

	private String prod;
	private String desc;
	private List<String> prices;
	private List<String> stocks;
	private List<String> names;
}
