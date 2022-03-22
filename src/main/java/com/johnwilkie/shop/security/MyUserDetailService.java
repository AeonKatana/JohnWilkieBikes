package com.johnwilkie.shop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.johnwilkie.shop.model.User;
import com.johnwilkie.shop.repository.UserRepo;

@Service
public class MyUserDetailService implements UserDetailsService {
  @Autowired
  private UserRepo repo;
  
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = this.repo.findByUsername(username);
    if (user == null)
      throw new UsernameNotFoundException("Could not found user with that Username"); 
    return new MyUserDetails(user);
  }
}

