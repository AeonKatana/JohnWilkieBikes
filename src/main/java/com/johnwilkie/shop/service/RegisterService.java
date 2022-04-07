package com.johnwilkie.shop.service;

import javax.servlet.http.HttpServletRequest;

import com.johnwilkie.shop.model.User;
import com.johnwilkie.shop.model.UserAddress;

public interface RegisterService {
  boolean register(User paramUser, UserAddress paramUserAddress, HttpServletRequest request);
}