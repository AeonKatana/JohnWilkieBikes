package com.johnwilkie.shop.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.johnwilkie.shop.model.Orders;
import com.johnwilkie.shop.model.User;

@Repository
public interface OrderRepo extends JpaRepository<Orders, Long> {
  Page<Orders> findAllByOrdertypeOrOrdertypeAndUserAndStatusNotAndStatusNotAndStatusNotAndStatusNot(String paramString,String paramString2 ,User paramUser, String status,String status2,String status3, String status4,Pageable paramPageable);
  List<Orders> findAllByOrdertypeAndUser(String paramString, User paramUser);
  long countByDatetime(LocalDateTime date);
  long countByMonthAndYear(String month, int year);
  long countByYear(int year);
  long countByStatusOrStatus(String status, String status2);
  
  long countByStatusOrStatusAndMonth(String status,String status2, String month);
  
  List<Orders> findAllByStatusNotOrStatusNotOrderByDatetimeDesc(String status , String status2);
  Page<Orders> findAllByUserAndStatusOrStatus(User user,String status ,String status2 , Pageable pageable);
List<Orders> findAllByStatusNotAndStatusNotOrderByDatetimeDesc(String string, String string2);
List<Orders> findTop5ByStatusNotAndStatusNotOrderByDatetimeDesc(String string, String string2);

  @Query("SELECT o from Orders o join o.user u where (o.ordertype =:ordertype or o.ordertype =:ordertype2) and o.status != 'CANCELLED'  "
  		+ "and o.status != 'DELIVERED' and o.status != 'PICKED UP' and o.status != 'CANCELLING' and u.id =:id")
  Page<Orders> getAllOrdersByUserAndOrderType(@Param("id") long id,@Param("ordertype") String ordertype, @Param("ordertype2") String ordertype2, Pageable pageable);

  @Query("SELECT o from Orders o where o.status = 'CANCELLED' or o.status = 'CANCELLING'")
  List<Orders> getAllCancelOrders();
  
  @Query("SELECT SUM(o.price) from Orders o join o.bikeprod bp where (o.status = 'DELIVERED' or o.status = 'PICKED UP' or o.ordertype = 'paypal') and bp.id =:id and o.month =:month")
  BigDecimal getBikeProductTotalPrice(@Param("id") long bikeid, @Param("month") String month);
  
  @Query("SELECT SUM(o.price) from Orders o where (o.status = 'DELIVERED' or o.status = 'PICKED UP' or o.ordertype = 'paypal')")
  BigDecimal getTotalSales();
  
  @Query("SELECT SUM(o.price) from Orders o where (o.status = 'DELIVERED' or o.status = 'PICKED UP' or o.ordertype = 'paypal') and o.month =:month")
  BigDecimal getMonthlySales(@Param("month") String month);
  
  @Query("SELECT SUM(o.price) from Orders o where (o.status = 'DELIVERED' or o.status = 'PICKED UP' or o.ordertype = 'paypal') and o.year =:year")
  BigDecimal getYearlySales(@Param("year") int year);
  
  @Query("SELECT DISTINCT o.month from Orders o")
  List<String> findAllMonths();
  
}