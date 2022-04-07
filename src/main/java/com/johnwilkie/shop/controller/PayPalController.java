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
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.johnwilkie.shop.model.BikeProdVariation;
import com.johnwilkie.shop.model.Cart;
import com.johnwilkie.shop.model.Orders;
import com.johnwilkie.shop.paypal.PayPalService;
import com.johnwilkie.shop.repository.BikeProdRepo;
import com.johnwilkie.shop.repository.BikeProdVariationRepo;
import com.johnwilkie.shop.repository.CartRepo;
import com.johnwilkie.shop.repository.OrderRepo;
import com.johnwilkie.shop.security.MyUserDetails;
import com.johnwilkie.shop.service.CartService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;

@Controller
@RequestMapping("/paypal")
public class PayPalController {

	
	@Autowired
	private PayPalService palService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private CartRepo cartRepo;
	
	@Autowired
	private OrderRepo orderrepo;
	
	@Autowired
	private BikeProdVariationRepo bikevarrepo;
	
	@Autowired
	private BikeProdRepo bikerepo;
	
	@Autowired
	private JavaMailSender sender;
	
	@PostMapping("/pay")
	public String pay(@AuthenticationPrincipal MyUserDetails user,  @RequestParam(required = false) String buynow,@RequestParam(required = false) Long bikeid,
			  @RequestParam(required = false) Long varid,@RequestParam(required = false) Integer qty, HttpServletRequest request) {
		double total = 0;
		String url = "http://" + request.getServerName() + ":" + request.getServerPort() + "/";
		List<Cart> usercart = null;
		if(buynow == null) {
			usercart = this.cartService.getUserCart(user.getUser());
		}
		else {
			Cart cart = new Cart();
	    	cart.setBikeprod(bikerepo.findById(bikeid).orElse(null));
	    	cart.setVariation(bikevarrepo.findById(varid).orElse(null));
	    	cart.setUserid(user.getUser());
	    	cart.setQuantity(qty);
	    	System.out.println("Buy Now Price :" + cart.getQtyPrice());
	    	usercart = Arrays.asList(cart);
	    	HttpSession session = request.getSession();
	    	session.setAttribute("buynowcart", usercart);
	    	session.setAttribute("buynow", buynow);
		}
		
	      for (Cart cartitem : usercart)
	        total += cartitem.getQtyPrice().doubleValue(); 
		try {
			Payment payment = palService.createPayment(total,"JW Bike Shop Order Payment", url + "paypal/pay/cancel", url + "paypal/pay/success");
			for(Links link : payment.getLinks()) {
				if(link.getRel().equals("approval_url")) {
					System.out.println(link.getHref());
					return "redirect:" + link.getHref();
				}
			}
			
		}catch(PayPalRESTException e) {
			
		}
		
		
		return "redirect:/";
	}
	
	@GetMapping("/pay/cancel")
	public String cancelPay() {
		return "cancelpaypal";
	}
	@GetMapping("/pay/success")
	@Transactional
	public String successPay(HttpSession session,HttpServletRequest request,@AuthenticationPrincipal MyUserDetails user,@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, Model model) {
		   
			try {
				Payment payment = palService.executePayment(paymentId, payerId);
				System.out.println(payment.toJSON());
				if(payment.getState().equals("approved")) {
					List<Cart> usercart = null;
					List<String> orderref = new ArrayList<>();
					if(session.getAttribute("buynowcart") == null)
					   usercart = cartRepo.getUserAvailableCartItems(user.getUser().getId());
					else {
						usercart = (List<Cart>)session.getAttribute("buynowcart");
					}
					   
				    
				    for (Cart cart : usercart) {
				     
				      BikeProdVariation bpv = cart.getVariation();
				      bpv.setStocks(bpv.getStocks() - cart.getQuantity());
				      bikevarrepo.save(bpv);
				      if(session.getAttribute("buynow") == null) {
				    	  System.out.println("Item to be deleted from cart :" + cart.getBikeprod().getProdname());
					      this.cartService.deleteCartItem(user.getUser(), cart.getBikeprod().getId(), cart.getVariation().getId());
				      }
				      Orders order = new Orders();
				      order.setUser(user.getUser());
				      order.setBikeprod(cart.getBikeprod());
				      order.setVariation(cart.getVariation().getName());
				      order.setMonth(ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Manila")).getMonth().name());
				      order.setDay(ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Manila")).getDayOfMonth());
				      order.setYear(ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Manila")).getYear());
				      order.setDatetime(ZonedDateTime.ofInstant(Instant.now(),ZoneId.of("Asia/Manila")).toLocalDateTime());
				      order.setQuantity(cart.getQuantity());
				      order.setOrdertype("paypal");
				      String refs = UUID.randomUUID().toString();
				      order.setOrdercode(refs);
				      orderref.add(refs);
				      order.setPrice(cart.getQtyPrice());
				      this.orderrepo.save(order);
				      
				      
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
				      url="http://" + request.getServerName() + ":" + request.getServerPort() + "/orders/mydelivery";
				      text = text.replace("[[URL]]", url);
				      try {
				    	 helper.setFrom("aeonkatana@gmail.com", "John Wilkie's Online Bike Shop");
				    	 helper.setSubject("JWBikes Order Update"); 
				    	 helper.setTo(user.getUser().getEmail());
				    	 helper.setText(text, true);
				    	 sender.send(message);
				      }catch(Exception e) {
				    	  
				      }
				      
				      String text2 = user.getUser().getFname() +" has placed an order/s using PayPal with the reference/s" + orderrefs +
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
				      
				      
				    }	
				    
				    
				   
				    
				    
					 model.addAttribute("orderType", "paypal");
					 
				for(Transaction t : payment.getTransactions()) {
					 System.out.println("Amount Paid : " + t.getAmount());
				}
					
					return "aftercheckout";
				}
			}catch(PayPalRESTException e) {
				System.out.println(e.getMessage());
			}
			return "redirect:/";
	}

	
	
	
}
