package com.johnwilkie.shop.model;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Orders {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  
  public Orders() {}
  
  public Orders(long id, String status, LocalDateTime datetime, BikeProduct bikeprod, User user, BigDecimal price, String ordertype, String variation, int quantity) {
    this.id = id;
    this.status = status;
    this.datetime = datetime;
    this.bikeprod = bikeprod;
    this.user = user;
    this.price = price;
    this.ordertype = ordertype;
    this.variation = variation;
    this.quantity = quantity;
  }
  
  public void setId(long id) {
    this.id = id;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
  
  public void setDatetime(LocalDateTime datetime) {
    this.datetime = datetime;
  }
  
  public void setBikeprod(BikeProduct bikeprod) {
    this.bikeprod = bikeprod;
  }
  
  public void setUser(User user) {
    this.user = user;
  }
  
  public void setPrice(BigDecimal price) {
    this.price = price;
  }
  
  public void setOrdertype(String ordertype) {
    this.ordertype = ordertype;
  }
  
  public void setVariation(String variation) {
    this.variation = variation;
  }
  
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
  
  public long getId() {
    return this.id;
  }
  
  public String getStatus() {
    return this.status;
  }
  
  public LocalDateTime getDatetime() {
    return this.datetime;
  }
  
  public BikeProduct getBikeprod() {
    return this.bikeprod;
  }
  
  public User getUser() {
    return this.user;
  }
  
  public BigDecimal getPrice() {
    return this.price;
  }
  
  public String getOrdertype() {
    return this.ordertype;
  }
  
  public String getVariation() {
    return this.variation;
  }
  
  public int getQuantity() {
    return this.quantity;
  }
  
  @Column(name = "order_status")
  private String status = "PROCESSING";
  
  @Column(name = "order_date")
  private LocalDateTime datetime;
  
  @ManyToOne
  @JoinColumn(name = "bike_id")
  private BikeProduct bikeprod;
  
  @ManyToOne
  @JoinColumn(name = "customer_id")
  private User user;
  
  private BigDecimal price;
  
  private String ordertype;
  
  private String variation;
  
  private int quantity;
}
