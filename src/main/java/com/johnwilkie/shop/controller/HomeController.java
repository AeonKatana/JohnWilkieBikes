package com.johnwilkie.shop.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.johnwilkie.shop.model.BikeProdVariation;
import com.johnwilkie.shop.model.BikeProduct;
import com.johnwilkie.shop.model.Cart;
import com.johnwilkie.shop.model.User;
import com.johnwilkie.shop.security.MyUserDetails;
import com.johnwilkie.shop.service.HomeService;
import com.johnwilkie.shop.service.OrderService;

@Controller
public class HomeController {
  @Autowired
  private HomeService homeservice;
  
  @Autowired
  private OrderService orderservice;
  
  @RequestMapping({"/"})
  public String homepage(Model model) {
    model.addAttribute("categories", this.homeservice.getAllCategories());
    return "homepage";
  }
  
  @GetMapping({"/search"})
  public String searchProducts(@RequestParam("search") String search, Model model) {
    model.addAttribute("currentPage", Integer.valueOf(1));
    model.addAttribute("products", this.homeservice.searchProduct(search, 0));
    model.addAttribute("totalpage", Integer.valueOf(this.homeservice.searchProduct(search, 0).getTotalPages()));
    model.addAttribute("parameter", search);
    model.addAttribute("totalitems", Long.valueOf(this.homeservice.searchProduct(search, 0).getTotalElements()));
    String url = "/search/page/";
    model.addAttribute("url", url);
    return "prodlist";
  }
  
  @GetMapping({"/search/page/{page}"})
  public String searchProducts(@RequestParam("search") String search, Model model, @PathVariable("page") int page) {
    model.addAttribute("currentPage", Integer.valueOf(page));
    model.addAttribute("products", this.homeservice.searchProduct(search, page - 1));
    model.addAttribute("totalpage", Integer.valueOf(this.homeservice.searchProduct(search, page - 1).getTotalPages()));
    model.addAttribute("parameter", search);
    model.addAttribute("totalitems", Long.valueOf(this.homeservice.searchProduct(search, 0).getTotalElements()));
    String url = "/search/page/";
    model.addAttribute("url", url);
    return "prodlist";
  }
  
  @RequestMapping({"/orders/mydelivery"})
  public String deliveryPage(@AuthenticationPrincipal MyUserDetails userdetail, Model model) {
    User user = userdetail.getUser();
    model.addAttribute("categories", this.homeservice.getAllCategories());
    model.addAttribute("currentPage", Integer.valueOf(1));
    model.addAttribute("what", "No orders for delivery");
    model.addAttribute("orders", this.orderservice.findAllByOrderTypeAndUser("cod", user, 0));
    model.addAttribute("totalpage", Integer.valueOf(this.orderservice.findAllByOrderTypeAndUser("cod", user, 0).getTotalPages()));
    System.out.println("Status : " + this.orderservice.findAllByOrderTypeAndUser("cod", user, 0).getTotalPages());
    model.addAttribute("url", "/orders/mydelivery/page/");
    System.out.println(this.orderservice.findAllByOrderTypeAndUser("cod", user, 0).getTotalPages());
    return "orders";
  }
  
  @RequestMapping({"/orders/mydelivery/page/{page}"})
  public String deliveryPage(@AuthenticationPrincipal MyUserDetails userdetail, Model model, @PathVariable("page") int page) {
    User user = userdetail.getUser();
    model.addAttribute("currentPage", Integer.valueOf(page));
    model.addAttribute("what", "No orders for delivery");
    model.addAttribute("orders", this.orderservice.findAllByOrderTypeAndUser("cod", user, page - 1));
    model.addAttribute("totalpage", Integer.valueOf(this.orderservice.findAllByOrderTypeAndUser("cod", user, page - 1).getTotalPages()));
    model.addAttribute("url", "/orders/mydelivery/page/");
    System.out.println(this.orderservice.findAllByOrderTypeAndUser("cod", user, page - 1).getTotalPages());
    return "orders";
  }
  
  @RequestMapping({"/orders/mypickup"})
  public String pickupPage(@AuthenticationPrincipal MyUserDetails userdetail, Model model) {
    User user = userdetail.getUser();
    System.out.println(user.getFname());
    model.addAttribute("currentPage", Integer.valueOf(1));
    model.addAttribute("what", "No orders for pickup");
    model.addAttribute("orders", this.orderservice.findAllByOrderTypeAndUser("cop", user, 0));
    model.addAttribute("totalpage", Integer.valueOf(this.orderservice.findAllByOrderTypeAndUser("cop", user, 0).getTotalPages()));
    System.out.println(" Status : " + this.orderservice.findAllByOrderTypeAndUser("cop", user, 0).getTotalPages());
    model.addAttribute("url", "/orders/mypickup/page/");
    System.out.println(this.orderservice.findAllByOrderTypeAndUser("cop", user, 0).getTotalPages());
    return "orders";
  }
  
  @RequestMapping({"/orders/mypickup/page/{page}"})
  public String pickupPage(@AuthenticationPrincipal MyUserDetails userdetail, Model model, @PathVariable("page") int page) {
    User user = userdetail.getUser();
    model.addAttribute("currentPage", Integer.valueOf(page));
    model.addAttribute("what", "No orders for pickup");
    model.addAttribute("orders", this.orderservice.findAllByOrderTypeAndUser("cop", user, page - 1));
    model.addAttribute("totalpage", Integer.valueOf(this.orderservice.findAllByOrderTypeAndUser("cop", user, page - 1).getTotalPages()));
    model.addAttribute("url", "/orders/mypickup/page/");
    System.out.println(this.orderservice.findAllByOrderTypeAndUser("cop", user, page - 1).getTotalPages());
    return "orders";
  }
  
  @RequestMapping({"/orders/mycompleted"})
  public String completePage(@AuthenticationPrincipal MyUserDetails userdetail, Model model) {
    User user = userdetail.getUser();
    System.out.println(user.getFname());
    model.addAttribute("currentPage", Integer.valueOf(1));
    model.addAttribute("what", "No completed orders yet");
    model.addAttribute("orders", this.orderservice.findAllByOrderTypeAndUser("done", user, 0));
    model.addAttribute("totalpage", Integer.valueOf(this.orderservice.findAllByOrderTypeAndUser("done", user, 0).getTotalPages()));
    System.out.println(" Status : " + this.orderservice.findAllByOrderTypeAndUser("done", user, 0).getTotalPages());
    model.addAttribute("url", "/orders/mycompleted/page/");
    System.out.println(this.orderservice.findAllByOrderTypeAndUser("done", user, 0).getTotalPages());
    return "orders";
  }
  
  @RequestMapping({"/orders/mycompleted/page/{page}"})
  public String completePage(@AuthenticationPrincipal MyUserDetails userdetail, Model model, @PathVariable("page") int page) {
    User user = userdetail.getUser();
    model.addAttribute("currentPage", Integer.valueOf(page));
    model.addAttribute("what", "No completed orders yet");
    model.addAttribute("orders", this.orderservice.findAllByOrderTypeAndUser("done", user, page - 1));
    model.addAttribute("totalpage", Integer.valueOf(this.orderservice.findAllByOrderTypeAndUser("done", user, page - 1).getTotalPages()));
    model.addAttribute("url", "/orders/mycompleted/page/");
    System.out.println(this.orderservice.findAllByOrderTypeAndUser("done", user, page - 1).getTotalPages());
    return "orders";
  }
  
  @RequestMapping({"/category/{categ}"})
  public String toBikeParts(Model model, @PathVariable("categ") String categoryname) {
    System.out.println(categoryname);
    String url = "/category/" + categoryname + "/page/";
    model.addAttribute("url", url);
    model.addAttribute("currentPage", Integer.valueOf(1));
    model.addAttribute("categories", this.homeservice.getAllCategories());
    model.addAttribute("categoryname", categoryname);
    model.addAttribute("products", this.homeservice.getAllByCategory(categoryname, 0));
    model.addAttribute("totalpage", Integer.valueOf(this.homeservice.getAllByCategory(categoryname, 0).getTotalPages()));
    return "prodlist";
  }
  
  @RequestMapping({"/category/{categ}/page/{page}"})
  public String toBikeParts(Model model, @PathVariable("categ") String categoryname, @PathVariable("page") int page) {
    System.out.println(categoryname);
    String url = "/category/" + categoryname + "/page/";
    model.addAttribute("url", url);
    model.addAttribute("currentPage", Integer.valueOf(page));
    model.addAttribute("categories", this.homeservice.getAllCategories());
    model.addAttribute("categoryname", categoryname);
    model.addAttribute("products", this.homeservice.getAllByCategory(categoryname, page - 1));
    model.addAttribute("totalpage", Integer.valueOf(this.homeservice.getAllByCategory(categoryname, page - 1).getTotalPages()));
    return "prodlist";
  }
  
  @RequestMapping({"/product/{id}"})
  public String productDetail(@PathVariable("id") long id, Model model, @AuthenticationPrincipal MyUserDetails userdetail) {
    model.addAttribute("categories", this.homeservice.getAllCategories());
    BikeProduct currentProd = this.homeservice.getProductDetail(id);
    User user = null;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) {
      model.addAttribute("user", null);
    } else {
      user = userdetail.getUser();
    } 
    Set<BikeProdVariation> var = currentProd.getVariation();
    model.addAttribute("user", user);
    model.addAttribute("bikeprod", currentProd);
    model.addAttribute("variation", var);
    return "viewprod";
  }
  
  @RequestMapping({"/mycart"})
  public String toMyCart(@AuthenticationPrincipal MyUserDetails userdetail, Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) {
      model.addAttribute("mycart", null);
    } else {
      User user = userdetail.getUser();
      List<Cart> mycart = this.homeservice.getUserAvailableCartItems(user);
      if (mycart.size() < 1) {
        model.addAttribute("mycart", Integer.valueOf(0));
      } else {
        model.addAttribute("mycart", mycart);
      } 
    } 
    return "mycart";
  }
}
