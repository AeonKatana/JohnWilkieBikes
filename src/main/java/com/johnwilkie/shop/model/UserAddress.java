package com.johnwilkie.shop.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class UserAddress {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  
  private String streetno;
  
  private String streetname;
  
  private String baranngay;
  
  private String city;
  
  private String province;
  
  private int zipcode;
  
  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;
  
  public UserAddress() {}
  
  public UserAddress(long id, String streetno, String streetname, String baranngay, String city, String province, int zipcode, User user) {
    this.id = id;
    this.streetno = streetno;
    this.streetname = streetname;
    this.baranngay = baranngay;
    this.city = city;
    this.province = province;
    this.zipcode = zipcode;
    this.user = user;
  }
  
  public void setId(long id) {
    this.id = id;
  }
  
  public void setStreetno(String streetno) {
    this.streetno = streetno;
  }
  
  public void setStreetname(String streetname) {
    this.streetname = streetname;
  }
  
  public void setBaranngay(String baranngay) {
    this.baranngay = baranngay;
  }
  
  public void setCity(String city) {
    this.city = city;
  }
  
  public void setProvince(String province) {
    this.province = province;
  }
  
  public void setZipcode(int zipcode) {
    this.zipcode = zipcode;
  }
  
  public void setUser(User user) {
    this.user = user;
  }
  
  public long getId() {
    return this.id;
  }
  
  public String getStreetno() {
    return this.streetno;
  }
  
  public String getStreetname() {
    return this.streetname;
  }
  
  public String getBaranngay() {
    return this.baranngay;
  }
  
  public String getCity() {
    return this.city;
  }
  
  public String getProvince() {
    return this.province;
  }
  
  public int getZipcode() {
    return this.zipcode;
  }
  
  public User getUser() {
    return this.user;
  }
}

