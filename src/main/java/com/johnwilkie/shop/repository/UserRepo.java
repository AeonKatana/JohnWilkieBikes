package com.johnwilkie.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.johnwilkie.shop.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
  @Query("SELECT u.email from User u WHERE u.email = :email")
  String findByUserEmail(@Param("email") String paramString);
  
  User findByUsername(String paramString);
  User findByVerifytoken(String token);
}


