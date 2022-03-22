package com.johnwilkie.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.johnwilkie.shop.model.User;
import com.johnwilkie.shop.model.UserAddress;
import com.johnwilkie.shop.repository.UserAddressRepo;
import com.johnwilkie.shop.repository.UserRepo;

@Service
public class RegisterServiceImp implements RegisterService {
  @Autowired
  private UserRepo userrepo;
  
  @Autowired
  private UserAddressRepo addressrepo;
  
  @Autowired
  private PasswordEncoder encoder;
  
  public String register(User user, UserAddress address) {
    user.setPassword(this.encoder.encode(String.valueOf(user.getPassword())).toCharArray());
    String checkusername = this.userrepo.findByUserEmail(user.getEmail());
    System.out.println(checkusername);
    System.out.println(user.getEmail());
    if (checkusername == null) {
      User newuser = (User)this.userrepo.save(user);
      address.setUser(newuser);
      this.addressrepo.save(address);
      return "Registered";
    } 
    return "Email already taken!";
  }
}


