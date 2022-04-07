package com.johnwilkie.shop.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.johnwilkie.shop.model.BikeProduct;
import com.johnwilkie.shop.model.Cart;
import com.johnwilkie.shop.model.ProdCategory;
import com.johnwilkie.shop.model.User;

public interface HomeService {
  Page<BikeProduct> getAllByCategory(String paramString, int paramInt, Sort sort);
  
  BikeProduct getProductDetail(long paramLong);
  
  List<Cart> getCartItems(User paramUser);
  
  List<Cart> getUserAvailableCartItems(User paramUser);
  
  List<ProdCategory> getAllCategories();
  
  int getAllProductStocks(long paramLong);
  
  Page<BikeProduct> searchProduct(String paramString, int paramInt, Sort sort);
  
  long categCount();
}