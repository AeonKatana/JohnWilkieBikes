package com.johnwilkie.shop.service;

import com.johnwilkie.shop.model.User;
import com.johnwilkie.shop.model.UserAddress;

public interface RegisterService {
  String register(User paramUser, UserAddress paramUserAddress);
}