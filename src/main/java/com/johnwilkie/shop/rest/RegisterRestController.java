package com.johnwilkie.shop.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.johnwilkie.shop.model.User;
import com.johnwilkie.shop.model.UserAddress;
import com.johnwilkie.shop.service.RegisterService;

@RestController
public class RegisterRestController {
  @Autowired
  private RegisterService registerservice;
  
  @PostMapping({"/register"})
  public String register(User user, UserAddress address) {
    String result = this.registerservice.register(user, address);
    if (result.equals("Registered"))
      return "Registered"; 
    return result;
  }
}

