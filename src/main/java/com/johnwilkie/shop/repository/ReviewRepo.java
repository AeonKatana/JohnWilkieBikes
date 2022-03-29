package com.johnwilkie.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.johnwilkie.shop.model.BikeProduct;
import com.johnwilkie.shop.model.Review;

public interface ReviewRepo extends JpaRepository<Review, Long> {

	Page<Review> findAllByBikeprod(BikeProduct product, Pageable pageable);
	
}
