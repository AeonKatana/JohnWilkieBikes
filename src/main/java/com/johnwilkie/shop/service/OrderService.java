package com.johnwilkie.shop.service;

import org.springframework.data.domain.Page;

import com.johnwilkie.shop.model.Orders;
import com.johnwilkie.shop.model.User;

public interface OrderService {
	  Page<Orders> findAllByOrderTypeAndUser(String paramString, User paramUser, int paramInt);
	}