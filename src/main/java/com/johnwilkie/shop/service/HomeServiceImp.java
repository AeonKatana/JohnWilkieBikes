package com.johnwilkie.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.johnwilkie.shop.model.BikeProduct;
import com.johnwilkie.shop.model.Cart;
import com.johnwilkie.shop.model.ProdCategory;
import com.johnwilkie.shop.model.User;
import com.johnwilkie.shop.repository.BikeProdRepo;
import com.johnwilkie.shop.repository.BikeProdVariationRepo;
import com.johnwilkie.shop.repository.CartRepo;
import com.johnwilkie.shop.repository.ProdCategoryRepo;

@Service
public class HomeServiceImp implements HomeService {
  @Autowired
  private BikeProdRepo bikerepo;
  
  @Autowired
  private CartRepo cartrepo;
  
  @Autowired
  private ProdCategoryRepo categoryrepo;
  
  @Autowired
  private BikeProdVariationRepo variationrepo;
  
  public Page<BikeProduct> getAllByCategory(String category, int page, Sort sort) {
    Page<BikeProduct> prodlist = this.bikerepo.getAllByCategory(category, (Pageable)PageRequest.of(page, 3 , sort.descending()));
    return prodlist;
  }
  
  public BikeProduct getProductDetail(long id) {
    BikeProduct thisProd = this.bikerepo.findById(Long.valueOf(id)).orElse(null);
    return thisProd;
  }
  
  public List<Cart> getCartItems(User user) {
    List<Cart> cart = this.cartrepo.findAllByUserid(user);
    return cart;
  }
  
  public List<Cart> getUserAvailableCartItems(User user) {
    List<Cart> cartavailable = this.cartrepo.getUserAvailableCartItems(user.getId());
    return cartavailable;
  }
  
  public List<ProdCategory> getAllCategories() {
    List<ProdCategory> categories = this.categoryrepo.findAll();
    return categories;
  }
  
  public int getAllProductStocks(long bikeid) {
    return this.variationrepo.getAllStocks(bikeid);
  }
  
  public Page<BikeProduct> searchProduct(String search, int page , Sort sort) {
    return this.bikerepo.findByProdnameContainingOrProddescContaining(search, search, (Pageable)PageRequest.of(page, 3, sort.descending()));
  }
}

