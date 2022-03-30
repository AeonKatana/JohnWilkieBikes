package com.johnwilkie.shop.controller;

import java.util.List;
import java.util.Set;

import javax.mail.BodyPart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import com.johnwilkie.shop.repository.BikeProdRepo;
import com.johnwilkie.shop.repository.ReviewRepo;
import com.johnwilkie.shop.security.MyUserDetails;
import com.johnwilkie.shop.service.HomeService;
import com.johnwilkie.shop.service.OrderService;

@Controller
public class HomeController {
  @Autowired
  private HomeService homeservice;
  
  @Autowired
  private OrderService orderservice;
  
  @Autowired
  private ReviewRepo reviewRepo;
  
  @Autowired
  private BikeProdRepo bikerepo;
  
  @RequestMapping({"/"})
  public String homepage(Model model) {
    model.addAttribute("categories", this.homeservice.getAllCategories());
  model.addAttribute("featured",bikerepo.findAllByFeatured(true, PageRequest.of(0, 10, Sort.by("prodrating").ascending())));
   model.addAttribute("onsale", bikerepo.findAllByProddiscoutGreaterThan(0F, PageRequest.of(0, 10)));
   model.addAttribute("onsalesize", bikerepo.findAllByProddiscoutGreaterThan(0F, PageRequest.of(0, 10)).getTotalPages());
   model.addAttribute("featuresize", bikerepo.findAllByFeatured(true, PageRequest.of(0, 10, Sort.by("prodrating"))).getSize());
    return "homepage";
  }
  
  @GetMapping({"/search"})
  public String searchProducts(@RequestParam("search") String search, Model model) {
    model.addAttribute("currentPage", Integer.valueOf(1));
    model.addAttribute("products", this.homeservice.searchProduct(search, 0, Sort.by("prodname")));
    model.addAttribute("totalpage", Integer.valueOf(this.homeservice.searchProduct(search, 0, Sort.by("prodname")).getTotalPages()));
    model.addAttribute("parameter", search);
    model.addAttribute("totalitems", Long.valueOf(this.homeservice.searchProduct(search, 0, Sort.by("prodname")).getTotalElements()));
    String url = "/search/sorted/page/";
    model.addAttribute("sortparam", "prodname");
    model.addAttribute("url", url);
    String sorturl = "/search/sorted?search=" + search + "&" + "sort=";
    model.addAttribute("sorturl", sorturl);
    return "prodlist";
  }
  
  @GetMapping({"/search/sorted/page/{page}"})
  public String searchProducts(@RequestParam("search") String search, @RequestParam("sort") String sort, Model model, @PathVariable("page") int page) {
    model.addAttribute("currentPage", Integer.valueOf(page));
    model.addAttribute("products", this.homeservice.searchProduct(search, page - 1 , Sort.by(sort)));
    model.addAttribute("totalpage", Integer.valueOf(this.homeservice.searchProduct(search, page - 1,Sort.by(sort)).getTotalPages()));
    model.addAttribute("parameter", search);
    model.addAttribute("totalitems", Long.valueOf(this.homeservice.searchProduct(search, page - 1,Sort.by(sort)).getTotalElements()));
    String url = "/search/sorted/page/";
    model.addAttribute("url", url);
    String sorturl = "/search/sorted?search=" + search + "&" + "sort=";
    model.addAttribute("sorturl", sorturl);
    model.addAttribute("sortparam", sort);
    return "prodlist";
  }
  
  @RequestMapping("/search/sorted")
  public String searchProducts(@RequestParam("search") String search,@RequestParam("sort") String sort , Model model)
  {
	  model.addAttribute("currentPage", Integer.valueOf(1));
	  model.addAttribute("products", this.homeservice.searchProduct(search, 0, Sort.by(sort)));
	  model.addAttribute("totalpage", Integer.valueOf(this.homeservice.searchProduct(search, 0, Sort.by(sort)).getTotalPages()));
	  model.addAttribute("parameter", search);
	  model.addAttribute("totalitems", Long.valueOf(this.homeservice.searchProduct(search, 0, Sort.by(sort)).getTotalElements()));
	  String url = "/search/sorted/page/";
	    model.addAttribute("url", url);
	    String sorturl = "/search/sorted?search=" + search + "&" + "sort=";
	    model.addAttribute("sorturl", sorturl);
	    model.addAttribute("sortparam", sort);
	    return "prodlist";
  }
  
  
  
 
  
  @RequestMapping({"/category/{categ}"})
  public String toBikeParts(Model model, @PathVariable("categ") String categoryname,  @RequestParam(required = false) String sort) {
    System.out.println(categoryname);
    String url = "/category/" + categoryname + "/page/";
    model.addAttribute("url", url);
    model.addAttribute("currentPage", Integer.valueOf(1));
    model.addAttribute("categories", this.homeservice.getAllCategories());
    model.addAttribute("categoryname", categoryname);
    model.addAttribute("products", homeservice.getAllByCategory(categoryname, 0, Sort.by("prodname")));
    model.addAttribute("totalpage", Integer.valueOf(this.homeservice.getAllByCategory(categoryname, 0 , Sort.by("prodname")).getTotalPages()));
    model.addAttribute("url", url);
    String sorturl = "/category/" + categoryname + "?sort=";
    if(sort == null) {
    	 model.addAttribute("sortparam", "prodname");
    }
    else {
    	model.addAttribute("sortparam", sort);
    }
    model.addAttribute("sorturl", sorturl);
    
    return "prodlist";
  }
  

  
  
  @RequestMapping({"/category/{categ}/page/{page}"})
  public String toBikeParts(Model model, @PathVariable("categ") String categoryname, @RequestParam("sort") String sort ,@PathVariable("page") int page) {
    System.out.println(categoryname);
    String url = "/category/" + categoryname + "/page/";
    model.addAttribute("url", url);
    model.addAttribute("currentPage", Integer.valueOf(page));
    model.addAttribute("categories", this.homeservice.getAllCategories());
    model.addAttribute("categoryname", categoryname);
    model.addAttribute("products", this.homeservice.getAllByCategory(categoryname, page - 1, Sort.by(sort)));
    model.addAttribute("totalpage", Integer.valueOf(this.homeservice.getAllByCategory(categoryname, page - 1, Sort.by(sort)).getTotalPages()));
    model.addAttribute("url", url);
    String sorturl = "/category/" + categoryname + "?sort=";
    model.addAttribute("sorturl", sorturl);
    model.addAttribute("sortparam", sort);


    return "prodlist";
  }
  
  @RequestMapping({"/product/{id}"})
  public String productDetail(@PathVariable("id") String id, Model model, @AuthenticationPrincipal MyUserDetails userdetail) {
    model.addAttribute("categories", this.homeservice.getAllCategories());
    BikeProduct currentProd = this.homeservice.getProductDetail(Long.parseLong(id));
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
    model.addAttribute("reviews",reviewRepo.findAllByBikeprod(currentProd, PageRequest.of(0, 5)));
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
