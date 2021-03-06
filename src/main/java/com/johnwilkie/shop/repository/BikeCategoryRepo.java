package com.johnwilkie.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.johnwilkie.shop.model.BikeCategory;
import com.johnwilkie.shop.model.BikeProduct;
import com.johnwilkie.shop.model.ProdCategory;

@Repository
public interface BikeCategoryRepo extends JpaRepository<BikeCategory, Long> {
	
	
	void deleteByBikeprodAndCategory(BikeProduct bp, ProdCategory categid);
	
	
}
