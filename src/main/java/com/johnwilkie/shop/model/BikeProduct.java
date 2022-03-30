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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bikeproduct")
public class BikeProduct {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  
  private String prodname;
  
  private String proddesc;
  
  @Column(name="prod_rating")
  private double prodrating;
  
  @Column(name = "prod_image")
  private String prodimgurl;
  
  @Column(name = "discount")
  private float proddiscout;
  
  private LocalDateTime datetime;
  
  private int timesordered;
  
  @OneToMany(mappedBy = "bikeprod")
  @Fetch(FetchMode.JOIN)
  private Set<BikeCategory> category;
  
  @OneToMany(mappedBy = "bikeprod", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
  @Fetch(FetchMode.JOIN)
  @JsonManagedReference
  private Set<Orders> orders;
  
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "bikeprod", cascade = {CascadeType.ALL})
  private Set<Cart> prodcart;
  
  @OneToMany(mappedBy = "bikeprod")
  @Fetch(FetchMode.JOIN)
  @JsonManagedReference
  private Set<BikeProdVariation> variation;
  
  @OneToMany(mappedBy = "bikeprod")
  @JsonManagedReference
  private Set<Review> reviews;
  
  @Transient
  @JsonSerialize
  private int prodstock;
  	
  @Transient
  @JsonSerialize
  private String pricerange;
  
  private boolean recommended;
  
  private boolean featured;
  
  
  @Transient
  public String getPriceRange() {
    List<BikeProdVariation> prices = new ArrayList<>(this.variation);
    BikeProdVariation low = Collections.<BikeProdVariation>min(prices);
    BikeProdVariation high = Collections.<BikeProdVariation>max(prices);
    return "₱" + low.getPrice() + " - ₱" + high.getPrice();
  }
  
  @Transient
  public int getProdstock() {
	int stocks = 0;
    for (BikeProdVariation var : this.variation)
      stocks += var.getStocks(); 
    return stocks;
  }
  
  @Transient
  public double getProdrating() {
	  double rating = 0;
	  for(Review r : reviews) {
		  rating += r.getRating();
	  }
	  return rating / reviews.size();
  }
  
  
}
