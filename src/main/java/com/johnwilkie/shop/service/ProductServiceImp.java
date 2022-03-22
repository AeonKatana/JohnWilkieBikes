package com.johnwilkie.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.johnwilkie.shop.model.BikeProduct;
import com.johnwilkie.shop.repository.BikeProdRepo;

@Service
public class ProductServiceImp implements ProductService {
  @Autowired
  private BikeProdRepo bikerepo;
  
  public BikeProduct selectedProd(Long id) {
    BikeProduct bikeprod = this.bikerepo.findById(id).orElse(null);
    return bikeprod;
  }
}

