package com.johnwilkie.shop.controller;

import java.time.LocalDateTime;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.johnwilkie.shop.model.Cart;
import com.johnwilkie.shop.model.Orders;
import com.johnwilkie.shop.model.User;
import com.johnwilkie.shop.repository.OrderRepo;
import com.johnwilkie.shop.security.MyUserDetails;
import com.johnwilkie.shop.service.CartService;

@RestController
public class CheckoutController {
  @Autowired
  private OrderRepo orderrepo;
  
  @Autowired
  private CartService cartservice;
  
  @Transactional
  @PostMapping({"/checkout/{orderType}"})
  public String checkoutCod(@AuthenticationPrincipal MyUserDetails userdetails, @PathVariable("orderType") String orderType) {
    LocalDateTime ldt = LocalDateTime.now();
    User sessionuser = userdetails.getUser();
    if (sessionuser == null)
      return "You're not logged in"; 
    Set<Cart> usercart = sessionuser.getUsercart();
    for (Cart cart : usercart) {
      this.cartservice.deleteCartItem(sessionuser, cart.getBikeprod().getId(), cart.getVariation().getId());
      Orders order = new Orders();
      order.setUser(sessionuser);
      order.setBikeprod(cart.getBikeprod());
      order.setVariation(cart.getVariation().getName());
      order.setDatetime(ldt);
      order.setQuantity(cart.getQuantity());
      order.setOrdertype(orderType);
      order.setPrice(cart.getQtyPrice());
      this.orderrepo.save(order);
    } 
    return "Order Successful";
  }
}

