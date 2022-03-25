package com.johnwilkie.shop.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.johnwilkie.shop.model.Cart;
import com.johnwilkie.shop.model.Orders;
import com.johnwilkie.shop.model.User;
import com.johnwilkie.shop.repository.CartRepo;
import com.johnwilkie.shop.repository.OrderRepo;
import com.johnwilkie.shop.security.MyUserDetails;
import com.johnwilkie.shop.service.CartService;
@Controller
@Transactional
public class CheckoutController {
  @Autowired
  private OrderRepo orderrepo;
  
  @Autowired
  private CartService cartservice;
  
  @Autowired
  private CartRepo cartrepo;
  
  @PostMapping({"/mycart/checkout/confirm"})
  public String checkoutCod(@AuthenticationPrincipal MyUserDetails userdetails,@RequestParam("orderType") String orderType, RedirectAttributes attr) {
    LocalDateTime ldt = LocalDateTime.now();
    User sessionuser = userdetails.getUser();
    System.out.println("Order Type :" + orderType);
    List<Cart> usercart = cartrepo.getUserAvailableCartItems(sessionuser.getId());
    for (Cart cart : usercart) {
      System.out.println("Item to be deleted from cart :" + cart.getBikeprod().getProdname());
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
    attr.addFlashAttribute("orderType", orderType);
    return "redirect:/mycart/checkout/success";
  }
  
  @GetMapping("/mycart/checkout/success")
  public String checkoutsuccess(@AuthenticationPrincipal MyUserDetails userdetails,@ModelAttribute("orderType") String orderType, Model model) {
	  if(orderType.equalsIgnoreCase("")) {
		  return "redirect:/mycart";
	  }
	  model.addAttribute("orderType" , orderType);
	  return "aftercheckout";
  }
}

