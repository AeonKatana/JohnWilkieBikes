package com.johnwilkie.shop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class BikeProdVariation implements Comparable<BikeProdVariation> {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  
  private String name;
  
  private int stocks;
  
  private BigDecimal price;
  
  @ManyToOne
  @JsonIgnore
  private BikeProduct bikeprod;
  
  @OneToMany(mappedBy = "variation")
  @JsonIgnore
  private Set<Cart> cart;
  
  public BikeProdVariation() {}
  
  public BikeProdVariation(long id, String name, int stocks, BigDecimal price, BikeProduct bikeprod, Set<Cart> cart) {
    this.id = id;
    this.name = name;
    this.stocks = stocks;
    this.price = price;
    this.bikeprod = bikeprod;
    this.cart = cart;
  }
  
  public void setId(long id) {
    this.id = id;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setStocks(int stocks) {
    this.stocks = stocks;
  }
  
  public void setPrice(BigDecimal price) {
    this.price = price;
  }
  
  public void setBikeprod(BikeProduct bikeprod) {
    this.bikeprod = bikeprod;
  }
  
  public void setCart(Set<Cart> cart) {
    this.cart = cart;
  }
  
  public long getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getStocks() {
    return this.stocks;
  }
  
  public BigDecimal getPrice() {
    return this.price;
  }
  
  public BikeProduct getBikeprod() {
    return this.bikeprod;
  }
  
  public Set<Cart> getCart() {
    return this.cart;
  }
  
  public int compareTo(BikeProdVariation o) {
    return this.price.compareTo(o.price);
  }
}
