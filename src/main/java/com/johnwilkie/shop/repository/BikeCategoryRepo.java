package com.johnwilkie.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.johnwilkie.shop.model.BikeCategory;

@Repository
public interface BikeCategoryRepo extends JpaRepository<BikeCategory, Long> {}
