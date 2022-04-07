package com.johnwilkie.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.johnwilkie.shop.model.ProdCategory;

public interface ProdCategoryRepo extends JpaRepository<ProdCategory, Long> {
	
	@Query("SELECT count(*) from ProdCategory pc join pc.category c")
	long countProductswithCategory();
	
}
