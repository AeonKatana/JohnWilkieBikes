package com.johnwilkie.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.johnwilkie.shop.model.ProdCategory;

public interface ProdCategoryRepo extends JpaRepository<ProdCategory, Long> {}
