package com.johnwilkie.shop.controller;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@PostMapping("/pay")
	public String pay(@AuthenticationPrincipal MyUserDetails user, HttpServletRequest request) {
		double total = 0;
		String url = "http://" + request.getServerName() + ":" + request.getServerPort() + "/";
		 List<Cart> usercart = this.cartService.getUserCart(user.getUser());
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
		return "cancel";
	}
	@GetMapping("/pay/success")
	@Transactional
	public String successPay(@AuthenticationPrincipal MyUserDetails user,@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, Model model) {
		   
			try {
				Payment payment = palService.executePayment(paymentId, payerId);
				System.out.println(payment.toJSON());
				if(payment.getState().equals("approved")) {
					
					
					List<Cart> usercart = cartRepo.getUserAvailableCartItems(user.getUser().getId());
				    
					   
				    
				    for (Cart cart : usercart) {
				     
				      BikeProdVariation bpv = cart.getVariation();
				      bpv.setStocks(bpv.getStocks() - cart.getQuantity());
				      bikevarrepo.save(bpv);
				      System.out.println("Item to be deleted from cart :" + cart.getBikeprod().getProdname());
				      this.cartService.deleteCartItem(user.getUser(), cart.getBikeprod().getId(), cart.getVariation().getId());
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
				      order.setOrdercode(UUID.randomUUID().toString());
				      order.setPrice(cart.getQtyPrice());
				      this.orderrepo.save(order);
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
