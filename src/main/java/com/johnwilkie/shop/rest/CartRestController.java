package com.johnwilkie.shop.rest;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.johnwilkie.shop.model.Cart;
import com.johnwilkie.shop.model.User;
import com.johnwilkie.shop.security.MyUserDetails;
import com.johnwilkie.shop.service.CartService;

@RestController
public class CartRestController {
  @Autowired
  private CartService cartservice;
  
  @GetMapping({"/mycart/totalQty"})
  public BigDecimal getTotalQty(@AuthenticationPrincipal MyUserDetails userdetail) {
    double total = 0.0D;
    User user = userdetail.getUser();
    if (user != null) {
      List<Cart> usercart = this.cartservice.getUserCart(user);
      for (Cart cartitem : usercart)
        total += cartitem.getQtyPrice().doubleValue(); 
      return new BigDecimal(total);
    } 
    return new BigDecimal(0);
  }
  
  @PutMapping({"/mycart/add/{id}/{varid}"})
  public Cart addCartItem(@PathVariable("id") String id, @PathVariable("varid") String varid, @AuthenticationPrincipal MyUserDetails userdetail) {
    User user = userdetail.getUser();
    Cart cart = this.cartservice.addCartItem(user, Long.parseLong(id), Long.parseLong(varid));
    return cart;
  }
  
  @PutMapping({"/mycart/minus/{id}/{varid}"})
  public Cart minusCartItem(@PathVariable("id") String id, @PathVariable("varid") String varid, @AuthenticationPrincipal MyUserDetails userdetail) {
    User user = userdetail.getUser();
    Cart cart = this.cartservice.minusCartItem(user, Long.parseLong(id), Long.parseLong(varid));
    return cart;
  }
  
  @Transactional
  @DeleteMapping({"/mycart/delete/{id}/{varid}"})
  public String deleteCartItem(@PathVariable("id") String id, @PathVariable("varid") String varid, @AuthenticationPrincipal MyUserDetails userdetail) {
    User user = userdetail.getUser();
    return this.cartservice.deleteCartItem(user, Long.parseLong(id), Long.parseLong(varid));
  }
}

