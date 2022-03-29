package com.johnwilkie.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.johnwilkie.shop.model.BikeProdVariation;
import com.johnwilkie.shop.model.BikeProduct;

@Repository
public interface BikeProdVariationRepo extends JpaRepository<BikeProdVariation, Long> {
  @Query("SELECT SUM(v.stocks) FROM BikeProdVariation v JOIN v.bikeprod bp WHERE bp.id = :bikeid")
  int getAllStocks(@Param("bikeid") long paramLong);
  
  BikeProdVariation findByBikeprodAndId(BikeProduct paramBikeProduct, long paramLong);
  
  long countByStocksLessThan(int number);
  
}

