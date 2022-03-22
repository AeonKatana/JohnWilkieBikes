package com.johnwilkie.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.johnwilkie.shop.model.BikeProduct;

@Repository
public interface BikeProdRepo extends JpaRepository<BikeProduct, Long> {
  @Query("SELECT bp FROM BikeProduct bp JOIN bp.category bc JOIN bc.category pc WHERE pc.categoryname = :category")
  Page<BikeProduct> getAllByCategory(@Param("category") String paramString, Pageable paramPageable);
  
  Page<BikeProduct> findByProdnameContainingOrProddescContaining(String paramString1, String paramString2, Pageable paramPageable);
}
