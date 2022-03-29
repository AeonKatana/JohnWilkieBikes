package com.johnwilkie.shop.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.johnwilkie.shop.model.Orders;
import com.johnwilkie.shop.model.User;

@Repository
public interface OrderRepo extends JpaRepository<Orders, Long> {
  Page<Orders> findAllByOrdertypeAndUserAndStatusNotAndStatusNotAndStatusNot(String paramString, User paramUser, String status,String status2,String status3,Pageable paramPageable);
  List<Orders> findAllByOrdertypeAndUser(String paramString, User paramUser);
  long countByDatetime(LocalDateTime date);
  long countByMonthAndYear(String month, int year);
  long countByYear(int year);
  List<Orders> findAllByStatusNotOrStatusNotOrderByDatetimeDesc(String status , String status2);
  Page<Orders> findAllByUserAndStatus(User user,String status , Pageable pageable);
List<Orders> findAllByStatusNotAndStatusNotOrderByDatetimeDesc(String string, String string2);
}