package com.johnwilkie.shop.model;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

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
public class BikeProdVariation implements Comparable<BikeProdVariation> {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  
  private String name;
  
  private int stocks;
  
  private BigDecimal price;
  
  @ManyToOne
  @JsonBackReference
  private BikeProduct bikeprod;
  
  @OneToMany(mappedBy = "variation")
  private Set<Cart> cart;
  
  @Transient
  private BigDecimal discountedprice;
  
  public BigDecimal getDiscountedprice() {
	  discountedprice = price.subtract(new BigDecimal(bikeprod.getProddiscout() / 100).multiply(price));
	  return discountedprice;
  }
  
  public int compareTo(BikeProdVariation o) {
    return this.price.compareTo(o.price);
  }
}
