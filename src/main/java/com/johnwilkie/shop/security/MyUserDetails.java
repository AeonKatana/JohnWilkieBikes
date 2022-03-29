package com.johnwilkie.shop.security;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.johnwilkie.shop.model.User;

public class MyUserDetails implements UserDetails {
  private User user;
  
  public User getUser() {
    return this.user;
  }
  
  public MyUserDetails(User user) {
    this.user = user;
  }
  
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return (Collection)Arrays.asList((Object[])new SimpleGrantedAuthority[] { new SimpleGrantedAuthority(this.user.getRole()) });
  }
  
  public String getPassword() {
    return String.valueOf(this.user.getPassword());
  }
  
  public String getUsername() {
    return this.user.getUsername();
  }
  
  public boolean isAccountNonExpired() {
    return true;
  }
  
  public boolean isAccountNonLocked() {
    return this.user.isLocked();
  }
  
  public boolean isCredentialsNonExpired() {
    return true;
  }
  
  public boolean isEnabled() {
    return this.user.isEnabled();
  }
}
