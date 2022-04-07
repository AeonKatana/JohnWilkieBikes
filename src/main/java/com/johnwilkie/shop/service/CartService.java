package com.johnwilkie.shop.service;

import java.math.BigDecimal;
import java.util.List;

import com.johnwilkie.shop.model.BikeProdVariation;
import com.johnwilkie.shop.model.BikeProduct;
import com.johnwilkie.shop.model.Cart;
import com.johnwilkie.shop.model.User;

public interface CartService {
  String addToCart(BikeProduct paramBikeProduct, BikeProdVariation paramBikeProdVariation, int paramInt, User paramUser);
  
  int getCurrentCartQty(BikeProduct paramBikeProduct, User paramUser);
  
  Cart getCurrentCartItem(BikeProduct paramBikeProduct, User paramUser);
  
  BigDecimal getTotalPrice(User paramUser);
  
  List<Cart> getUserCart(User paramUser);
  
  Cart addCartItem(User paramUser, long paramLong1, long paramLong2);
  
  Cart minusCartItem(User paramUser, long paramLong1, long paramLong2);
  
  String deleteCartItem(User paramUser, long paramLong1, long paramLong2);
}