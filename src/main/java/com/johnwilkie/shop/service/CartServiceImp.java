package com.johnwilkie.shop.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.johnwilkie.shop.model.BikeProdVariation;
import com.johnwilkie.shop.model.BikeProduct;
import com.johnwilkie.shop.model.Cart;
import com.johnwilkie.shop.model.User;
import com.johnwilkie.shop.repository.BikeProdRepo;
import com.johnwilkie.shop.repository.BikeProdVariationRepo;
import com.johnwilkie.shop.repository.CartRepo;

@Service
public class CartServiceImp implements CartService {
  @Autowired
  private CartRepo cartrepo;
  
  @Autowired
  private BikeProdRepo bikerepo;
  
  @Autowired
  private BikeProdVariationRepo variationrepo;
  
  public String addToCart(BikeProduct bikeprod, BikeProdVariation var, int qty, User user) {
    Cart cart = this.cartrepo.findByBikeprodAndVariationAndUserid(bikeprod, var, user);
    if (cart == null) {
      Cart newcart = new Cart();
      newcart.setBikeprod(bikeprod);
      newcart.setVariation(var);
      newcart.setUserid(user);
      newcart.setQuantity(qty);
      this.cartrepo.save(newcart);
      return String.valueOf(qty) + "item/s succesfully added to cart!";
    } 
    cart.setId(cart.getId());
    cart.setUserid(user);
    if (cart.getQuantity() < var.getStocks()) {
      if (cart.getQuantity() + qty > var.getStocks()) {
        cart.setQuantity(cart.getQuantity() + qty - cart.getQuantity() + qty - var.getStocks());
      } else {
        cart.setQuantity(cart.getQuantity() + qty);
      } 
      this.cartrepo.save(cart);
      return "Cart succesfully updated!";
    } 
    return "You've reached the total amount of stocks in your cart for this specific item!";
  }
  
  public int getCurrentCartQty(BikeProduct bikeprod, User user) {
    Cart cart = this.cartrepo.findByBikeprodAndUserid(bikeprod, user);
    int qty = cart.getQuantity();
    return qty;
  }
  
  public Cart getCurrentCartItem(BikeProduct bikeprod, User user) {
    return this.cartrepo.findByBikeprodAndUserid(bikeprod, user);
  }
  
  @Deprecated
  public BigDecimal getTotalPrice(User user) {
    double total = 0.0D;
    Set<Cart> cartitems = user.getUsercart();
    for (Cart cartitem : cartitems)
      total += cartitem.getQtyPrice().doubleValue(); 
    return new BigDecimal(total);
  }
  
  public List<Cart> getUserCart(User user) {
    List<Cart> usercart = this.cartrepo.getUserAvailableCartItems(user.getId());
    return usercart;
  }
  
  public Cart addCartItem(User user, long id, long varid) {
    BikeProduct thisprod = this.bikerepo.findById(Long.valueOf(id)).orElse(null);
    BikeProdVariation var = this.variationrepo.findById(Long.valueOf(varid)).orElse(null);
    Cart cart = this.cartrepo.findByBikeprodAndVariationAndUserid(thisprod, var, user);
    cart.setId(cart.getId());
    cart.setVariation(var);
    cart.setQuantity(cart.getQuantity() + 1);
    cart.setUserid(user);
    cart.setBikeprod(thisprod);
    return (Cart)this.cartrepo.save(cart);
  }
  
  public Cart minusCartItem(User user, long id, long varid) {
    BikeProduct thisprod = this.bikerepo.findById(Long.valueOf(id)).orElse(null);
    BikeProdVariation var = this.variationrepo.findById(Long.valueOf(varid)).orElse(null);
    Cart cart = this.cartrepo.findByBikeprodAndVariationAndUserid(thisprod, var, user);
    cart.setId(cart.getId());
    cart.setVariation(var);
    cart.setQuantity(cart.getQuantity() - 1);
    cart.setUserid(user);
    cart.setBikeprod(thisprod);
    return (Cart)this.cartrepo.save(cart);
  }
  
  public String deleteCartItem(User user, long id, long varid) {
    BikeProduct product = this.bikerepo.findById(Long.valueOf(id)).orElse(null);
    BikeProdVariation var = this.variationrepo.findById(Long.valueOf(varid)).orElse(null);
    this.cartrepo.deleteByBikeprodAndUseridAndVariation(product, user, var);
    return "Successfully Removed!";
  }
}

