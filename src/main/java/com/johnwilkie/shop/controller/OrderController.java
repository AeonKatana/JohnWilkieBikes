package com.johnwilkie.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.johnwilkie.shop.model.User;
import com.johnwilkie.shop.security.MyUserDetails;
import com.johnwilkie.shop.service.OrderService;

@Controller
@RequestMapping({"/orders"})
public class OrderController {
  @Autowired
  private OrderService orderservice;
  
  @GetMapping({"/delivery/page/{page}"})
  public String deliveryOrders(@AuthenticationPrincipal MyUserDetails userdetail, Model model, @PathVariable("page") int page) {
    User user = userdetail.getUser();
    model.addAttribute("orders", this.orderservice.findAllByOrderTypeAndUser("cod", user, page - 1));
    return "orders";
  }
  
  @GetMapping({"/pickup"})
  public String pickupOrders(@AuthenticationPrincipal MyUserDetails userdetail, Model model) {
    User user = userdetail.getUser();
    model.addAttribute("orders", this.orderservice.findAllByOrderTypeAndUser("cop", user, 0));
    return "orders";
  }
  
  @GetMapping({"/pickup/page/{page}"})
  public String pickupOrders(@AuthenticationPrincipal MyUserDetails userdetail, Model model, @PathVariable("page") int page) {
    User user = userdetail.getUser();
    model.addAttribute("orders", this.orderservice.findAllByOrderTypeAndUser("cop", user, page - 1));
    return "orders";
  }
  
  @GetMapping({"/completed"})
  public String completeOrders() {
    return "orders";
  }
  
  @GetMapping({"/cancelled"})
  public String cancelledOrders() {
    return "orders";
  }
}
