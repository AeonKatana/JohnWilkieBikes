package com.johnwilkie.shop.model;

import java.util.Arrays;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  
  private String fname;
  
  private String lname;
  
  private String role;
  
  private String username;
  
  private char[] password;
  
  private String email;
  


private String verifytoken;
  
private String passwordtoken;


public String getVerifytoken() {
	return verifytoken;
}

public void setVerifytoken(String verifytoken) {
	this.verifytoken = verifytoken;
}




public String getPasswordtoken() {
	return passwordtoken;
}

public void setPasswordtoken(String passwordtoken) {
	this.passwordtoken = passwordtoken;
}




private long contactno;
  
  private boolean enabled;
  
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "userid", cascade = {CascadeType.ALL}, orphanRemoval = true)
  private Set<Cart> usercart;
  
  @OneToMany(mappedBy = "user")
  private Set<Orders> orders;
  
  @OneToOne(mappedBy = "user")
  private UserAddress address;
  
  public void setId(long id) {
    this.id = id;
  }
  
  public void setFname(String fname) {
    this.fname = fname;
  }
  
  public void setLname(String lname) {
    this.lname = lname;
  }
  
  public void setRole(String role) {
    this.role = role;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  public void setPassword(char[] password) {
    this.password = password;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public void setContactno(long contactno) {
    this.contactno = contactno;
  }
  
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
  
  public void setUsercart(Set<Cart> usercart) {
    this.usercart = usercart;
  }
  
  public void setOrders(Set<Orders> orders) {
    this.orders = orders;
  }
  
  public void setAddress(UserAddress address) {
    this.address = address;
  }
  
  public long getId() {
    return this.id;
  }
  
  public String getFname() {
    return this.fname;
  }
  
  public String getLname() {
    return this.lname;
  }
  
  public String getRole() {
    return this.role;
  }
  
  public String getUsername() {
    return this.username;
  }
  
  public char[] getPassword() {
    return this.password;
  }
  
  public String getEmail() {
    return this.email;
  }
  
  public long getContactno() {
    return this.contactno;
  }
  
  public boolean isEnabled() {
    return this.enabled;
  }
  
  public Set<Cart> getUsercart() {
    return this.usercart;
  }
  
  public Set<Orders> getOrders() {
    return this.orders;
  }
  
  public UserAddress getAddress() {
    return this.address;
  }
  
  public User(long id, String fname, String lname, String role, String username, char[] password, String email, long contactno, boolean enabled, Set<Cart> usercart, Set<Orders> orders, UserAddress address) {
    this.id = id;
    this.fname = fname;
    this.lname = lname;
    this.role = role;
    this.username = username;
    this.password = password;
    this.email = email;
    this.contactno = contactno;
    this.enabled = enabled;
    this.usercart = usercart;
    this.orders = orders;
    this.address = address;
  }
  
  public User() {}
  
  public static UserBuilder builder() {
    return new UserBuilder();
  }
  
  public static class UserBuilder {
    private long id;
    
    private String fname;
    
    private String lname;
    
    private String role;
    
    private String username;
    
    private char[] password;
    
    private String email;
    
    private long contactno;
    
    private boolean enabled;
    
    private Set<Cart> usercart;
    
    private Set<Orders> orders;
    
    private UserAddress address;
    
    public UserBuilder id(long id) {
      this.id = id;
      return this;
    }
    
    public UserBuilder fname(String fname) {
      this.fname = fname;
      return this;
    }
    
    public UserBuilder lname(String lname) {
      this.lname = lname;
      return this;
    }
    
    public UserBuilder role(String role) {
      this.role = role;
      return this;
    }
    
    public UserBuilder username(String username) {
      this.username = username;
      return this;
    }
    
    public UserBuilder password(char[] password) {
      this.password = password;
      return this;
    }
    
    public UserBuilder email(String email) {
      this.email = email;
      return this;
    }
    
    public UserBuilder contactno(long contactno) {
      this.contactno = contactno;
      return this;
    }
    
    public UserBuilder enabled(boolean enabled) {
      this.enabled = enabled;
      return this;
    }
    
    public UserBuilder usercart(Set<Cart> usercart) {
      this.usercart = usercart;
      return this;
    }
    
    public UserBuilder orders(Set<Orders> orders) {
      this.orders = orders;
      return this;
    }
    
    public UserBuilder address(UserAddress address) {
      this.address = address;
      return this;
    }
    
    public User build() {
      return new User(this.id, this.fname, this.lname, this.role, this.username, this.password, this.email, this.contactno, this.enabled, this.usercart, this.orders, this.address);
    }
    
    public String toString() {
      return "User.UserBuilder(id=" + this.id + ", fname=" + this.fname + ", lname=" + this.lname + ", role=" + this.role + ", username=" + this.username + ", password=" + Arrays.toString(this.password) + ", email=" + this.email + ", contactno=" + this.contactno + ", enabled=" + this.enabled + ", usercart=" + this.usercart + ", orders=" + this.orders + ", address=" + this.address + ")";
    }
  }
}
