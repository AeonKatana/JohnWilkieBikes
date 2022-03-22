package com.johnwilkie.shop.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.johnwilkie.shop.model.BikeProdVariation;
import com.johnwilkie.shop.model.BikeProduct;
import com.johnwilkie.shop.model.User;
import com.johnwilkie.shop.repository.BikeProdVariationRepo;
import com.johnwilkie.shop.security.MyUserDetails;
import com.johnwilkie.shop.service.CartService;
import com.johnwilkie.shop.service.ProductService;

@RestController
public class ProductController {
  @Autowired
  private ProductService prodservice;
  
  @Autowired
  private CartService cartservice;
  
  @Autowired
  private BikeProdVariationRepo variationrepo;
  
  @GetMapping({"/product/{id}/{varid}"})
  public BikeProdVariation selectVariation(@PathVariable("id") long id, @PathVariable("varid") long varid) {
    BikeProduct p = this.prodservice.selectedProd(Long.valueOf(id));
    BikeProdVariation var = this.variationrepo.findByBikeprodAndId(p, varid);
    return var;
  }
  
  @PostMapping({"/addtocart/{id}/{qty}"})
  public boolean checkUserBeforeaddToCart(@PathVariable("id") long id, @PathVariable("qty") int qty) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) {
      System.out.println("WTF");
      return false;
    } 
    System.out.println("Yo!");
    return true;
  }
  
  @PostMapping({"/addtocart/{id}/{varid}/{qty}/allow"})
  public String addToCart(@PathVariable("id") long id, @PathVariable("varid") long varid, @PathVariable("qty") int qty, @AuthenticationPrincipal MyUserDetails userdetails) {
    User user = userdetails.getUser();
    BikeProduct bikeprod = this.prodservice.selectedProd(Long.valueOf(id));
    BikeProdVariation var = this.variationrepo.findById(Long.valueOf(varid)).orElse(null);
    return this.cartservice.addToCart(bikeprod, var, qty, user);
  }
}

