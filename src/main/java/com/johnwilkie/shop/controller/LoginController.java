package com.johnwilkie.shop.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.johnwilkie.shop.dto.ForgotModel;
import com.johnwilkie.shop.model.PasswordModel;
import com.johnwilkie.shop.model.User;
import com.johnwilkie.shop.service.LoginService;

@Controller
public class LoginController {

  
	@Autowired
	private LoginService loginservice;
	
	
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
  
  
  
  @PostMapping("/forgotpass")
  @ResponseBody
  public boolean forgotPass(@RequestBody ForgotModel model, HttpServletRequest request) {
	  return loginservice.forgotPass(model.getEmail(), request);
  }
  
  
  @GetMapping("/resetPass")
  public String resetPass(@RequestParam("token") String token, Model model) {
	  User user = loginservice.findByPasswordToken(token);
	  if(user == null) {
		  model.addAttribute("user", null);
	  }
	  else {
		  model.addAttribute("user", user);
	  }
	  return "resetpass";
  }
  
  @PostMapping("/reset")
  public String resetpassword(RedirectAttributes attr, PasswordModel model) {
	  attr.addFlashAttribute("token",model.getToken());
	  loginservice.newPassword(model);
	  return "redirect:/resetcomp";
  }
  
  @GetMapping("/resetcomp")
  public String resetcomp(@ModelAttribute("token") String token) {
	  if(token.equalsIgnoreCase("")) {
		  return "redirect:/";
	  }
	  return "resetcomp";
  }
  
  
}
