package com.johnwilkie.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.johnwilkie.shop.model.User;
import com.johnwilkie.shop.repository.UserRepo;

@Service
public class LoginServiceImp implements LoginService {
  @Autowired
  private UserRepo repo;
  
  public User loginService(String username, char[] password) {
    return null;
  }
}
