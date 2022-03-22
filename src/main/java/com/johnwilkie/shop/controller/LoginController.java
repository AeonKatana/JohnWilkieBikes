package com.johnwilkie.shop.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {
  @RequestMapping({"/register-page"})
  public String registerPage() {
    return "register";
  }
  
  @RequestMapping({"/perform-logout"})
  public String logout() {
    return "";
  }
  
  @RequestMapping(value = {"/perform-login"}, method = {RequestMethod.POST})
  public String login() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)
      return "loginform"; 
    return "redirect:/";
  }
  
  @RequestMapping({"/login-page"})
  public String loginPage() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)
      return "loginform"; 
    return "redirect:/";
  }
}
