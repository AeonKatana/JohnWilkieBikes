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
  
  public Page<Orders> findAllByOrderTypeAndUser(String orderType, String ordertype , User user, String status, String status2, String status3 ,int page) {
    return this.orderrepo.getAllOrdersByUserAndOrderType(user.getId(), orderType ,ordertype, PageRequest.of(page, 5));
  }

@Override
public Page<Orders> findAllByUserAndStatus(User user, String status ,String status2 , int page) {
	return orderrepo.findAllByUserAndStatusOrStatus(user, status ,status2,PageRequest.of(page, 5));
}
}