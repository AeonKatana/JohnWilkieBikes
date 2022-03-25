package com.johnwilkie.shop.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class ProdCategory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  
  @Column(name = "category")
  private String categoryname;
  
  @OneToMany(mappedBy = "category")
  private Set<BikeCategory> category;
  
  public ProdCategory(long id, String categoryname, Set<BikeCategory> category) {
    this.id = id;
    this.categoryname = categoryname;
    this.category = category;
  }
  
  public ProdCategory() {}
  
  public void setId(long id) {
    this.id = id;
  }
  
  public void setCategoryname(String categoryname) {
    this.categoryname = categoryname;
  }
  
  public void setCategory(Set<BikeCategory> category) {
    this.category = category;
  }
  
  public long getId() {
    return this.id;
  }
  
  public String getCategoryname() {
    return this.categoryname;
  }
  
  public Set<BikeCategory> getCategory() {
    return this.category;
  }
}
