package com.johnwilkie.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.johnwilkie.shop.model.UserAddress;

public interface UserAddressRepo extends JpaRepository<UserAddress, Long> {}
