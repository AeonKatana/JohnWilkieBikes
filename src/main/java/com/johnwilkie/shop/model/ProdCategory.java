package com.johnwilkie.shop.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProdCategory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  
  @Column(name = "category")
  private String categoryname;
  
  @OneToMany(mappedBy = "category" , cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<BikeCategory> category;
  
  @Transient
  public int countprod() {
	  return category.size();
  }
  
}
