package com.johnwilkie.shop.controller;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.johnwilkie.shop.model.Orders;
import com.johnwilkie.shop.model.User;
import com.johnwilkie.shop.repository.OrderRepo;
import com.johnwilkie.shop.repository.ReviewRepo;
import com.johnwilkie.shop.security.MyUserDetails;
import com.johnwilkie.shop.service.HomeService;
import com.johnwilkie.shop.service.OrderService;

@Controller
@RequestMapping({"/orders"})
public class OrderController {
  @Autowired
  private OrderService orderservice;
  
  @Autowired
  private OrderRepo orderRepo;
  
  @Autowired
  private HomeService homeservice;
  
  @Autowired
  private ReviewRepo reviewrepo;
  
  @Autowired
  private JavaMailSender sender;
  
  @RequestMapping({"/mydelivery/page/{page}"})
  public String deliveryPage(@AuthenticationPrincipal MyUserDetails userdetail, Model model, @PathVariable("page") int page) {
    User user = userdetail.getUser();
    model.addAttribute("currentPage", Integer.valueOf(page));
    model.addAttribute("what", "No orders for delivery");
    model.addAttribute("orders", this.orderservice.findAllByOrderTypeAndUser("cod","paypap", user,"CANCELLED", "DELIVERED", "PICKED UP" , page - 1));
    model.addAttribute("totalpage", Integer.valueOf(this.orderservice.findAllByOrderTypeAndUser("cod","paypal" ,user,"CANCELLED", "DELIVERED", "PICKED UP" , page - 1).getTotalPages()));
    model.addAttribute("url", "/orders/mydelivery/page/");
    System.out.println(this.orderservice.findAllByOrderTypeAndUser("cod","paypal" ,user,"CANCELLED", "DELIVERED", "PICKED UP" , page - 1).getTotalPages());
    return "orders";
  }
  
  @RequestMapping({"/mypickup/page/{page}"})
  public String pickupPage(@AuthenticationPrincipal MyUserDetails userdetail, Model model, @PathVariable("page") int page) {
    User user = userdetail.getUser();
    model.addAttribute("currentPage", Integer.valueOf(page));
    model.addAttribute("what", "No orders for pickup");
    model.addAttribute("orders", this.orderservice.findAllByOrderTypeAndUser("cop","", user,"CANCELLED", "DELIVERED", "PICKED UP" , page - 1));
    model.addAttribute("totalpage", Integer.valueOf(this.orderservice.findAllByOrderTypeAndUser("cop", "",user,"CANCELLED", "DELIVERED", "PICKED UP" , page - 1).getTotalPages()));
    model.addAttribute("url", "/orders/mypickup/page/");
    System.out.println(this.orderservice.findAllByOrderTypeAndUser("cop", "",user,"CANCELLED", "DELIVERED", "PICKED UP" , page - 1).getTotalPages());
    return "orders";
  }
  
  @RequestMapping({"/mydelivery"})
  public String deliveryPage(@AuthenticationPrincipal MyUserDetails userdetail, Model model) {
    User user = userdetail.getUser();
    model.addAttribute("categories", this.homeservice.getAllCategories());
    model.addAttribute("currentPage", Integer.valueOf(1));
    model.addAttribute("what", "No orders for delivery");
    model.addAttribute("orders", this.orderservice.findAllByOrderTypeAndUser("cod", "paypal",user,"CANCELLED", "DELIVERED", "PICKED UP" ,0));
    model.addAttribute("totalpage", Integer.valueOf(this.orderservice.findAllByOrderTypeAndUser("cod", "paypal",user,"CANCELLED", "DELIVERED", "PICKED UP" , 0).getTotalPages()));
    System.out.println("Status : " + this.orderservice.findAllByOrderTypeAndUser("cod", "paypal",user,"CANCELLED", "DELIVERED", "PICKED UP" , 0).getTotalPages());
    model.addAttribute("url", "/orders/mydelivery/page/");
    System.out.println(this.orderservice.findAllByOrderTypeAndUser("cod","paypal", user,"CANCELLED", "DELIVERED", "PICKED UP" , 0).getTotalPages());
    return "orders";
  }
  
  
  
  @RequestMapping({"/mypickup"})
  public String pickupPage(@AuthenticationPrincipal MyUserDetails userdetail, Model model) {
    User user = userdetail.getUser();
    System.out.println(user.getFname());
    model.addAttribute("currentPage", Integer.valueOf(1));
    model.addAttribute("what", "No orders for pickup");
    model.addAttribute("orders", this.orderservice.findAllByOrderTypeAndUser("cop","" ,user,"CANCELLED", "DELIVERED", "PICKED UP" , 0));
    model.addAttribute("totalpage", Integer.valueOf(this.orderservice.findAllByOrderTypeAndUser("cop", "",user,"CANCELLED", "DELIVERED", "PICKED UP" , 0).getTotalPages()));
    System.out.println(" Status : " + this.orderservice.findAllByOrderTypeAndUser("cop", "" ,user,"CANCELLED", "DELIVERED", "PICKED UP" , 0).getTotalPages());
    model.addAttribute("url", "/orders/mypickup/page/");
    System.out.println(this.orderservice.findAllByOrderTypeAndUser("cop", "", user,"CANCELLED", "DELIVERED", "PICKED UP" , 0).getTotalPages());
    return "orders";
  }
  
  
  
  @RequestMapping({"/mycompleted"})
  public String completePage(@AuthenticationPrincipal MyUserDetails userdetail, Model model) {
    User user = userdetail.getUser();
    System.out.println(user.getFname());
    model.addAttribute("currentPage", Integer.valueOf(1));
    model.addAttribute("what", "No completed orders yet");
    model.addAttribute("orders", this.orderservice.findAllByUserAndStatus( user, "DELIVERED" ,"", 0));
    model.addAttribute("totalpage", Integer.valueOf(this.orderservice.findAllByUserAndStatus( user, "DELIVERED" ,"",0).getTotalPages()));
    System.out.println(" Status : " + this.orderservice.findAllByUserAndStatus( user, "DELIVERED","" ,0).getTotalPages());
    model.addAttribute("url", "/orders/mycompleted/page/");
    System.out.println(this.orderservice.findAllByUserAndStatus( user, "DELIVERED","" ,0).getTotalPages());
   
    
    
    return "orders";
  }
  
  @RequestMapping({"/mycompleted/page/{page}"})
  public String completePage(@AuthenticationPrincipal MyUserDetails userdetail, Model model, @PathVariable("page") int page) {
    User user = userdetail.getUser();
    model.addAttribute("currentPage", Integer.valueOf(page));
    model.addAttribute("what", "No completed orders yet");
    model.addAttribute("orders", this.orderservice.findAllByUserAndStatus( user, "DELIVERED" ,"",page - 1));
    model.addAttribute("totalpage", Integer.valueOf(this.orderservice.findAllByUserAndStatus( user, "DELIVERED","" ,page - 1).getTotalPages()));
    model.addAttribute("url", "/orders/mycompleted/page/");
    System.out.println(this.orderservice.findAllByUserAndStatus( user, "DELIVERED","" ,page - 1).getTotalPages());
    return "orders";
  }
  
  @GetMapping({"/cancelled"})
  public String cancelledOrders(@AuthenticationPrincipal MyUserDetails userdetail, Model model) {
	  User user = userdetail.getUser();
	    System.out.println(user.getFname());
	    model.addAttribute("currentPage", Integer.valueOf(1));
	    model.addAttribute("what", "No Cancelled Orders");
	    model.addAttribute("orders", this.orderservice.findAllByUserAndStatus(user, "CANCELLED", "CANCELLING" ,0));
	    model.addAttribute("totalpage", Integer.valueOf(this.orderservice.findAllByUserAndStatus(user,"CANCELLED","CANCELLING", 0).getTotalPages()));
	    System.out.println(" Status : " + this.orderservice.findAllByUserAndStatus(user, "CANCELLED","CANCELLING" ,0).getTotalPages());
	    model.addAttribute("url", "/orders/cancelled/page/");
	    System.out.println(this.orderservice.findAllByUserAndStatus(user, "CANCELLED" ,"CANCELLING" ,0).getTotalPages());
    return "orders";
  }
  @GetMapping({"/cancelled/page/{page}"})
  public String cancelledOrders(@AuthenticationPrincipal MyUserDetails userdetail, Model model, @PathVariable("page") int page) {
	  User user = userdetail.getUser();
	    System.out.println(user.getFname());
	    model.addAttribute("currentPage", Integer.valueOf(page));
	    model.addAttribute("what", "No Cancelled Orders");
	    model.addAttribute("orders", this.orderservice.findAllByUserAndStatus(user, "CANCELLED", "CANCELLING",page - 1));
	    model.addAttribute("totalpage", Integer.valueOf(this.orderservice.findAllByUserAndStatus(user,"CANCELLED","CANCELLING", page - 1).getTotalPages()));
	    System.out.println(" Status : " + this.orderservice.findAllByUserAndStatus(user, "CANCELLED","CANCELLING" ,page - 1).getTotalPages());
	    model.addAttribute("url", "/orders/cancelled/page/");
	    System.out.println(this.orderservice.findAllByUserAndStatus(user, "CANCELLED", "CANCELLING" ,page - 1).getTotalPages());
    return "orders";
  }
  
  
  
  @PutMapping("/cancelorder/{id}")
  @ResponseBody
  public String cancelOrder(HttpServletRequest request,@RequestParam("status") String status,@PathVariable("id") long id,@AuthenticationPrincipal MyUserDetails user) {
	  MimeMessage message = sender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message);
      
    
      
      
	    Orders order = orderRepo.findById(id).orElse(null);
	    
	    
	    String text = user.getUser().getFname() +" " + user.getUser().getLname() + " has requested to cancel his/her order"
	    		+ "<p> " + order.getBikeprod().getProdname() + " : " + order.getVariation() + " with the reference number of <span style=\"color: lightblue;\">" + order.getOrdercode() +"</span> </p>"
	    		+ "<p>You may check the cancellation request by clicking the link down below</p>"
	    		+ "<h3><a href=\"[[URL]]\" target=\"self\">View Cancellations</a></h3>";
	    	String url="http://" + request.getServerName() + ":" + request.getServerPort() + "/admin/cancels";
	      text = text.replace("[[URL]]", url);
	      try {
		    	 helper.setFrom("aeonkatana@gmail.com", "John Wilkie's Online Bike Shop");
		    	 helper.setSubject("JWBikes Order Update"); 
		    	 helper.setTo("aeonkatana@gmail.com");
		    	 helper.setText(text, true);
		    	 sender.send(message);
		      }catch(Exception e) {
		    	  
		  }
	    
		order.setStatus(status);
		orderRepo.save(order);
		return "Success";
	  
  }
  
  
}
