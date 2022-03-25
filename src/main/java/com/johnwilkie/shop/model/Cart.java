package com.johnwilkie.shop.model;


import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Cart {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  
  @ManyToOne
  @JsonIgnore
  @JoinColumn(name = "bike_product_id")
  private BikeProduct bikeprod;
  
  @ManyToOne
  @JsonIgnore
  @JoinColumn(name = "prod_variation")
  private BikeProdVariation variation;
  
  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "user_id")
  private User userid;
  
  private int quantity;
  
  @Transient
  private BigDecimal qtyPrice;
  
  public Cart(long id, BikeProduct bikeprod, BikeProdVariation variation, User userid, int quantity, BigDecimal qtyPrice) {
    this.id = 1000L;
    this.id = id;
    this.bikeprod = bikeprod;
    this.variation = variation;
    this.userid = userid;
    this.quantity = quantity;
    this.qtyPrice = qtyPrice;
  }
  
  public Cart() {
    this.id = 1000L;
  }
  
  public void setId(long id) {
    this.id = id;
  }
  
  public void setBikeprod(BikeProduct bikeprod) {
    this.bikeprod = bikeprod;
  }
  
  public void setVariation(BikeProdVariation variation) {
    this.variation = variation;
  }
  
  public void setUserid(User userid) {
    this.userid = userid;
  }
  
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
  
  public void setQtyPrice(BigDecimal qtyPrice) {
    this.qtyPrice = qtyPrice;
  }
  
  public long getId() {
    return this.id;
  }
  
  public BikeProduct getBikeprod() {
    return this.bikeprod;
  }
  
  public BikeProdVariation getVariation() {
    return this.variation;
  }
  
  public User getUserid() {
    return this.userid;
  }
  
  public int getQuantity() {
    return this.quantity;
  }
  
  @Transient
  public BigDecimal getQtyPrice() {
    BikeProdVariation variation = getVariation();
    this.qtyPrice = variation.getDiscountedprice().multiply(new BigDecimal(this.quantity));
    return this.qtyPrice;
  }
}
