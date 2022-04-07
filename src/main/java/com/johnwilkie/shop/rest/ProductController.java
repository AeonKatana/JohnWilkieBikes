package com.johnwilkie.shop.rest;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.johnwilkie.shop.dto.ReviewModel;
import com.johnwilkie.shop.model.BikeProdVariation;
import com.johnwilkie.shop.model.BikeProduct;
import com.johnwilkie.shop.model.Orders;
import com.johnwilkie.shop.model.Review;
import com.johnwilkie.shop.model.User;
import com.johnwilkie.shop.repository.BikeProdRepo;
import com.johnwilkie.shop.repository.BikeProdVariationRepo;
import com.johnwilkie.shop.repository.OrderRepo;
import com.johnwilkie.shop.repository.ReviewRepo;
import com.johnwilkie.shop.security.MyUserDetails;
import com.johnwilkie.shop.service.CartService;
import com.johnwilkie.shop.service.ProductService;

@RestController
public class ProductController {
  @Autowired
  private ProductService prodservice;
  
  @Autowired
  private CartService cartservice;
  
  @Autowired
  private BikeProdVariationRepo variationrepo;
  
  @Autowired
  private BikeProdRepo bikerepo;
  
  @Autowired
  private ReviewRepo reviewrepo;
  
  @Autowired
  private OrderRepo orderRepo;
  
  @GetMapping({"/product/{id}/{varid}"})
  public BikeProdVariation selectVariation(@PathVariable("id") long id, @PathVariable("varid") long varid) {
    BikeProduct p = this.prodservice.selectedProd(Long.valueOf(id));
    BikeProdVariation var = this.variationrepo.findByBikeprodAndId(p, varid);
    return var;
  }
  
  @PostMapping({"/addtocart/{id}/{qty}"})
  public boolean checkUserBeforeaddToCart(@PathVariable("id") long id, @PathVariable("qty") int qty) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) {
      System.out.println("WTF");
      return false;
    } 
    System.out.println("Yo!");
    return true;
  }
  
  @PostMapping({"/addtocart/{id}/{varid}/{qty}/allow"})
  public String addToCart(@PathVariable("id") long id, @PathVariable("varid") long varid, @PathVariable("qty") int qty, @AuthenticationPrincipal MyUserDetails userdetails) {
    User user = userdetails.getUser();
    BikeProduct bikeprod = this.prodservice.selectedProd(Long.valueOf(id));
    BikeProdVariation var = this.variationrepo.findById(Long.valueOf(varid)).orElse(null);
    return this.cartservice.addToCart(bikeprod, var, qty, user);
  }
  
  
  @PostMapping("/buynow")
  public boolean buynow() {
	  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication == null || authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) {
	      System.out.println("WTF");
	      return false;
	    } 
	    System.out.println("Yo!");
	    return true;
  }
  
  @PostMapping("/product/addReview/{id}/{oid}")
  public String addReview(@PathVariable("oid") long oid,@PathVariable("id") long id, @AuthenticationPrincipal MyUserDetails user, @RequestBody ReviewModel model) {
	  BikeProduct bp = bikerepo.findById(id).orElse(null);
	  Review review = new Review();
	  review.setUser(user.getUser());
	  review.setDatetime(ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Manila")).toLocalDateTime());
	  review.setReviewmessage(model.getMessage());
	  review.setBikeprod(bp);
	 
	  review.setRating(model.getRating());
	  Orders order = orderRepo.findById(oid).orElse(null);
	  review.setVariation(order.getVariation());
	  order.setReviewed(true);
	  orderRepo.save(order);
	  reviewrepo.save(review);
	  return "Review Success!";
  }
  
  
}

