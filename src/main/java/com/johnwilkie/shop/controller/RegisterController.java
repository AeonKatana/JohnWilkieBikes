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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.johnwilkie.shop.model.User;
import com.johnwilkie.shop.model.UserAddress;
import com.johnwilkie.shop.repository.UserRepo;
import com.johnwilkie.shop.service.RegisterService;

@Controller
public class RegisterController {
   @Autowired
   private RegisterService registerservice;
   
   @Autowired
   private UserRepo userrepo;
   
  @PostMapping({"/register"})
  private String registercomplete(User user, UserAddress address, RedirectAttributes attr , HttpServletRequest request) {
	  
	  boolean result = registerservice.register(user, address, request);
	  if(result) {
		  attr.addFlashAttribute("email", user.getFname());
		  return "redirect:/register-complete";
	  }
	  	  attr.addFlashAttribute("user", user);
	  	  attr.addFlashAttribute("address", address);
	  	  attr.addFlashAttribute("error", "yes");
	  	  return "redirect:/register-page";
  }
  
  @GetMapping("/register-complete")
  public String redirectComplete(@ModelAttribute("email")String firstname, Model model) {
	  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication == null || authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) {
	    	 model.addAttribute("name", firstname);
	    	 System.out.println("Name : " + firstname);
	   	  
	   	  if(firstname.equalsIgnoreCase("")) {
	   		  return "redirect:/register-page";
	   	  }
	   	  return "registercomplete";
	    }
	    	
	   return "redirect:/";
  }
  
  @RequestMapping({"/register-page"})
  public String registerPage(@ModelAttribute("error")String error, Model model) {
	  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication == null || authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) {
	    	if(error.equalsIgnoreCase("")) {
	  		  model.addAttribute("error", "no");
	  	  }
	  	  else {
	  		  model.addAttribute("error", true);
	  	  }
	  	  	System.out.println("Boolean :" + error);
	  	  	return "register";
	    }
	  return "redirect:/";
  }
  
  @RequestMapping("/verify")
  public String verify(@RequestParam("token") String token, Model model) {
	  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication == null || authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) {
	    	User user = userrepo.findByVerifytoken(token);
	    	if(user != null) {
	    		user.setEnabled(true);
	    		user.setVerifytoken(null);
	    		userrepo.save(user);
	    	}
	    	model.addAttribute("user", user);
	  	    return "verified"; 
	    }
	   return "redirect:/";
  }
  
}
