package com.johnwilkie.shop.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class BikeCategory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  
  @ManyToOne
  @JoinColumn(name = "category")
  @JsonIgnoreProperties("category")
  private ProdCategory category;
  
  @ManyToOne
  @JoinColumn(name = "bike_id")
  @JsonIgnore
  private BikeProduct bikeprod;
  
  public BikeCategory() {}
  
  public BikeCategory(long id, ProdCategory category, BikeProduct bikeprod) {
    this.id = id;
    this.category = category;
    this.bikeprod = bikeprod;
  }
  
  public void setId(long id) {
    this.id = id;
  }
  
  public void setCategory(ProdCategory category) {
    this.category = category;
  }
  
  public void setBikeprod(BikeProduct bikeprod) {
    this.bikeprod = bikeprod;
  }
  
  public long getId() {
    return this.id;
  }
  
  public ProdCategory getCategory() {
    return this.category;
  }
  
  public BikeProduct getBikeprod() {
    return this.bikeprod;
  }
}
