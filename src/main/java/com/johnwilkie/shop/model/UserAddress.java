package com.johnwilkie.shop.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
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
  @JsonBackReference
  @JoinColumn(name = "user_id")
  private User user;
  
 
}

