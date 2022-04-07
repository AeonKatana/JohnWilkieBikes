package com.johnwilkie.shop.controller;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.johnwilkie.shop.model.BikeProdVariation;
import com.johnwilkie.shop.model.BikeProduct;
import com.johnwilkie.shop.model.Cart;
import com.johnwilkie.shop.model.Orders;
import com.johnwilkie.shop.model.User;
import com.johnwilkie.shop.repository.BikeProdRepo;
import com.johnwilkie.shop.repository.BikeProdVariationRepo;
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
  
  @Autowired
  private BikeProdVariationRepo bikevarrepo;
  
  @Autowired
  private BikeProdRepo bikerepo;

  @Autowired
  private JavaMailSender sender;
  
  @PostMapping({"/mycart/checkout/confirm"})
  public String checkoutCod(@AuthenticationPrincipal MyUserDetails userdetails,@RequestParam("orderType") String orderType,
		  @RequestParam(required = false) String buynow,@RequestParam(required = false) Long bikeid,
		  @RequestParam(required = false) Long varid,@RequestParam(required = false) Integer qty, RedirectAttributes attr) {
    User sessionuser = userdetails.getUser();
    System.out.println("Status of :"  +buynow);
    List<Cart> usercart = null;
    if(buynow == null) {
    	usercart = cartrepo.getUserAvailableCartItems(sessionuser.getId());
    }else {
    	Cart cart = new Cart();
    	cart.setBikeprod(bikerepo.findById(bikeid).orElse(null));
    	cart.setVariation(bikevarrepo.findById(varid).orElse(null));
    	cart.setUserid(sessionuser);
    	cart.setQuantity(qty);
    	System.out.println("Buy Now Price :" + cart.getQtyPrice());
    	usercart = Arrays.asList(cart);
    }
    
    System.out.println("Order Type :" + orderType);
   
    List<String> orderrefs = new ArrayList<>();
    List<String> orders = new ArrayList<>();
    for (Cart cart : usercart) {
     
    	BikeProduct bp = cart.getBikeprod();
    	bp.setTimesordered(bp.getTimesordered() + cart.getQuantity());
      BikeProdVariation bpv = cart.getVariation();
      bpv.setStocks(bpv.getStocks() - cart.getQuantity());
      bikevarrepo.save(bpv);
      if(buynow == null) {
    	  System.out.println("Item to be deleted from cart :" + cart.getBikeprod().getProdname());
          this.cartservice.deleteCartItem(sessionuser, cart.getBikeprod().getId(), cart.getVariation().getId());
      }
      Orders order = new Orders();
      order.setUser(sessionuser);
      order.setBikeprod(cart.getBikeprod());
      order.setVariation(cart.getVariation().getName());
      order.setMonth(ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Manila")).getMonth().name());
      order.setDay(ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Manila")).getDayOfMonth());
      order.setYear(ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Manila")).getYear());
      order.setDatetime(ZonedDateTime.ofInstant(Instant.now(),ZoneId.of("Asia/Manila")).toLocalDateTime());
      order.setQuantity(cart.getQuantity());
      order.setOrdertype(orderType);
      String refs = UUID.randomUUID().toString();
      orderrefs.add(refs);
      order.setOrdercode(refs);
      order.setPrice(cart.getQtyPrice());
      this.orderrepo.save(order);
    } 
    attr.addFlashAttribute("orderType", orderType);
    attr.addFlashAttribute("orderref", orderrefs);
    attr.addFlashAttribute("orders", orders);
    return "redirect:/mycart/checkout/success";
  }
  
  @GetMapping("/mycart/checkout/success")
  public String checkoutsuccess(@ModelAttribute("orders") List<String> orders,@AuthenticationPrincipal MyUserDetails userdetails,@ModelAttribute("orderref") List<String> orderref,@ModelAttribute("orderType") String orderType, Model model,
		  						HttpServletRequest request) {
	  if(orderType.equalsIgnoreCase("")) {
		  return "redirect:/mycart";
	  }
	  
	  MimeMessage message = sender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message);
      
      MimeMessage message2 = sender.createMimeMessage();
      MimeMessageHelper helper2 = new MimeMessageHelper(message2);
      
      String orderrefs = "";
      for(String s : orderref) {
    	  orderrefs += " " + s;
      }
      
      String text = "Your order/s with the reference/s: " + orderrefs + " has been placed. <p>Please wait for updates for your order soon.</p>" +
       " \nClick the link to view your orders <h3><a href=\"[[URL]]\" target=\"self\"> View my orders </a></h3>"
    +  " \n From John Wilkie's Bike Shop";
      String url = "";
      if(orderType.equalsIgnoreCase("cop")) {
    	  url = "http://" + request.getServerName() + ":" + request.getServerPort() + "/orders/mypickup";
      }
      else
    	  url="http://" + request.getServerName() + ":" + request.getServerPort() + "/orders/mydelivery";
      text = text.replace("[[URL]]", url);
      try {
    	 helper.setFrom("aeonkatana@gmail.com", "John Wilkie's Online Bike Shop");
    	 helper.setSubject("JWBikes Order Update"); 
    	 helper.setTo(userdetails.getUser().getEmail());
    	 helper.setText(text, true);
    	 sender.send(message);
      }catch(Exception e) {
    	  
      }
      
      String text2 = userdetails.getUser().getFname() +" has placed an order/s with the reference/s" + orderrefs +
     " <p>You may start checking the orders on the link below and start preparing</p>" +
    " \nClick the link to view the orders  <h3><a href=\"[[URL]]\" target=\"self\"> View my orders </a></h3>";
      String url2 = "http://" + request.getServerName() + ":" + request.getServerPort() + "/admin/orders";
      text2 = text2.replace("[[URL]]", url2);
      try {
    	 helper2.setFrom("aeonkatana@gmail.com", "John Wilkie's Online Bike Shop");
    	 helper2.setSubject("JWBikes Order Update"); 
    	 helper2.setTo("aeonkatana@gmail.com");
    	 helper2.setText(text2, true);
    	 sender.send(message2);
      }catch(Exception e) {
    	  
      }
      
      
	  
	  model.addAttribute("orderref", orderref);
	  model.addAttribute("orderType" , orderType);
	  return "aftercheckout";
  }
}
