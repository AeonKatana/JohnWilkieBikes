package com.johnwilkie.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.johnwilkie.shop.model.BikeProdVariation;
import com.johnwilkie.shop.model.BikeProduct;
import com.johnwilkie.shop.model.Cart;
import com.johnwilkie.shop.model.User;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
  Cart findByBikeprodAndUserid(BikeProduct paramBikeProduct, User paramUser);
  
  Cart findByBikeprodAndVariationAndUserid(BikeProduct paramBikeProduct, BikeProdVariation paramBikeProdVariation, User paramUser);
  
  List<Cart> findAllByUserid(User paramUser);
  
  @Query("SELECT DISTINCT c from Cart c join c.bikeprod bp join c.variation v join c.userid u where v.stocks != 0 AND u.id = :user")
  List<Cart> getUserAvailableCartItems(@Param("user") long paramLong);
  
  void deleteByBikeprodAndUserid(BikeProduct paramBikeProduct, User paramUser);
  
  void deleteByBikeprodAndUseridAndVariation(BikeProduct paramBikeProduct, User paramUser, BikeProdVariation paramBikeProdVariation);
}
