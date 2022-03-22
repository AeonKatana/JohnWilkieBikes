package com.johnwilkie.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.johnwilkie.shop.model.Orders;
import com.johnwilkie.shop.model.User;
import com.johnwilkie.shop.repository.OrderRepo;

@Service
public class OrderServiceImp implements OrderService {
  @Autowired
  private OrderRepo orderrepo;
  
  public Page<Orders> findAllByOrderTypeAndUser(String orderType, User user, int page) {
    return this.orderrepo.findAllByOrdertypeAndUser(orderType, user, (Pageable)PageRequest.of(page, 5));
  }
}