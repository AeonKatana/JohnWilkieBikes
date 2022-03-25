package com.johnwilkie.shop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.johnwilkie.shop.model.Orders;
import com.johnwilkie.shop.model.User;

@Repository
public interface OrderRepo extends JpaRepository<Orders, Long> {
  Page<Orders> findAllByOrdertypeAndUser(String paramString, User paramUser, Pageable paramPageable);
  List<Orders> findAllByOrdertypeAndUser(String paramString, User paramUser);
 
}