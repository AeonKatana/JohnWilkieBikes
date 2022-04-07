package com.johnwilkie.shop.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.johnwilkie.shop.model.Cart;
import com.johnwilkie.shop.model.User;
import com.johnwilkie.shop.security.MyUserDetails;
import com.johnwilkie.shop.service.CartService;
import com.johnwilkie.shop.service.HomeService;

@Controller
public class CartController {
  @Autowired
  private CartService cartservice;
  
  @Autowired
  private HomeService homeservice;
  
  @RequestMapping({"/mycart/checkout"})
  public String toCheckoutPage(@AuthenticationPrincipal MyUserDetails userdetail, Model model) {
    User user = userdetail.getUser();
    List<Cart> cart = homeservice.getUserAvailableCartItems(user);
    System.out.println(cart);
    if(cart.isEmpty()) {
    	return "redirect:/mycart";
    }
    model.addAttribute("cartcheck", cart);
    model.addAttribute("user", user);
    return "checkout";
  }
}
