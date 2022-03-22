package com.johnwilkie.shop.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "bikeproduct")
public class BikeProduct {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  
  private String prodname;
  
  private String proddesc;
  
  @Column(name = "prod_rating")
  private double prodrating;
  
  @Column(name = "prod_image")
  private String prodimgurl;
  
  @Column(name = "discount")
  private float proddiscout;
  
  private LocalDateTime datetime;
  
  @OneToMany(mappedBy = "bikeprod")
  private Set<BikeCategory> category;
  
  @OneToMany(mappedBy = "bikeprod", cascade = {CascadeType.ALL})
  private Set<Orders> orders;
  
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "bikeprod", cascade = {CascadeType.ALL})
  private Set<Cart> prodcart;
  
  @OneToMany(mappedBy = "bikeprod")
  private Set<BikeProdVariation> variation;
  
  @Transient
  private int prodstock;
  
  @Transient
  private String pricerange;
  
  public void setId(long id) {
    this.id = id;
  }
  
  public void setProdname(String prodname) {
    this.prodname = prodname;
  }
  
  public void setProddesc(String proddesc) {
    this.proddesc = proddesc;
  }
  
  public void setProdrating(double prodrating) {
    this.prodrating = prodrating;
  }
  
  public void setProdimgurl(String prodimgurl) {
    this.prodimgurl = prodimgurl;
  }
  
  public void setProddiscout(float proddiscout) {
    this.proddiscout = proddiscout;
  }
  
  public void setDatetime(LocalDateTime datetime) {
    this.datetime = datetime;
  }
  
  public void setCategory(Set<BikeCategory> category) {
    this.category = category;
  }
  
  public void setOrders(Set<Orders> orders) {
    this.orders = orders;
  }
  
  public void setProdcart(Set<Cart> prodcart) {
    this.prodcart = prodcart;
  }
  
  public void setVariation(Set<BikeProdVariation> variation) {
    this.variation = variation;
  }
  
  public void setProdstock(int prodstock) {
    this.prodstock = prodstock;
  }
  
  public void setPricerange(String pricerange) {
    this.pricerange = pricerange;
  }
  
  public long getId() {
    return this.id;
  }
  
  public String getProdname() {
    return this.prodname;
  }
  
  public String getProddesc() {
    return this.proddesc;
  }
  
  public double getProdrating() {
    return this.prodrating;
  }
  
  public String getProdimgurl() {
    return this.prodimgurl;
  }
  
  public float getProddiscout() {
    return this.proddiscout;
  }
  
  public LocalDateTime getDatetime() {
    return this.datetime;
  }
  
  public Set<BikeCategory> getCategory() {
    return this.category;
  }
  
  public Set<Orders> getOrders() {
    return this.orders;
  }
  
  public Set<Cart> getProdcart() {
    return this.prodcart;
  }
  
  public Set<BikeProdVariation> getVariation() {
    return this.variation;
  }
  
  public int getProdstock() {
    return this.prodstock;
  }
  
  public BikeProduct() {}
  
  public BikeProduct(long id, String prodname, String proddesc, double prodrating, String prodimgurl, float proddiscout, LocalDateTime datetime, Set<BikeCategory> category, Set<Orders> orders, Set<Cart> prodcart, Set<BikeProdVariation> variation, int prodstock, String pricerange) {
    this.id = id;
    this.prodname = prodname;
    this.proddesc = proddesc;
    this.prodrating = prodrating;
    this.prodimgurl = prodimgurl;
    this.proddiscout = proddiscout;
    this.datetime = datetime;
    this.category = category;
    this.orders = orders;
    this.prodcart = prodcart;
    this.variation = variation;
    this.prodstock = prodstock;
    this.pricerange = pricerange;
  }
  
  @Transient
  public String getPriceRange() {
    List<BikeProdVariation> prices = new ArrayList<>(this.variation);
    BikeProdVariation low = Collections.<BikeProdVariation>min(prices);
    BikeProdVariation high = Collections.<BikeProdVariation>max(prices);
    return "₱" + low.getPrice() + " - ₱" + high.getPrice();
  }
  
  @Transient
  public int getProdStocks() {
    for (BikeProdVariation var : this.variation)
      this.prodstock += var.getStocks(); 
    return this.prodstock;
  }
}
