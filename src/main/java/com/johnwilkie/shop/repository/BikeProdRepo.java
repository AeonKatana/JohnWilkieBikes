package com.johnwilkie.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.johnwilkie.shop.model.BikeProduct;

@Repository
public interface BikeProdRepo extends JpaRepository<BikeProduct, Long> {
  @Query("SELECT bp FROM BikeProduct bp JOIN bp.category bc JOIN bc.category pc WHERE pc.categoryname = :category")
  Page<BikeProduct> getAllByCategory(@Param("category") String paramString, Pageable paramPageable);
  
  @Query("SELECT bp FROM BikeProduct bp JOIN bp.category bc JOIN bc.category pc WHERE pc.categoryname = :category order by bp.prodrating desc")
  Page<BikeProduct> getAllByCategoryOrderByProdratingDesc(@Param("category") String paramString, Pageable paramPageable);
  
  @Query("SELECT bp FROM BikeProduct bp JOIN bp.category bc JOIN bc.category pc JOIN bp.variation v WHERE pc.categoryname = :category order by v.price asc")
  Page<BikeProduct> getAllByCategoryOrderByPriceAsc(@Param("category") String paramString, Pageable paramPageable);
  
  @Query("SELECT bp FROM BikeProduct bp JOIN bp.category bc JOIN bc.category pc JOIN bp.variation v WHERE pc.categoryname = :category order by v.price desc")
  Page<BikeProduct> getAllByCategoryOrderByPriceDesc(@Param("category") String paramString, Pageable paramPageable);
  
  @Query("SELECT bp FROM BikeProduct bp JOIN bp.category bc JOIN bc.category pc WHERE pc.categoryname = :category order by bp.timesordered desc")
  Page<BikeProduct> getAllByCategoryOrderByTimesorderedDesc(@Param("category") String paramString, Pageable paramPageable);
  
  @Query("SELECT bp FROM BikeProduct bp JOIN bp.category bc JOIN bc.category pc WHERE pc.categoryname = :category order by bp.datetime desc")
  Page<BikeProduct> getAllByCategoryOrderByDatetimeDesc(@Param("category") String paramString, Pageable paramPageable);
 
  @Query("SELECT bp FROM BikeProduct bp JOIN bp.category bc JOIN bc.category pc WHERE pc.categoryname = :category order by bp.datetime asc")
  Page<BikeProduct> getAllByCategoryOrderByDatetimeAsc(@Param("category") String paramString, Pageable paramPageable);
  
  Page<BikeProduct> findByProdnameContainingOrProddescContaining(String paramString1, String paramString2, Pageable paramPageable);

  Page<BikeProduct> findAllByFeatured(boolean b, Pageable pageable);

 Page<BikeProduct> findAllByProddiscoutGreaterThan(float discount, Pageable pageable);
  
//  Page<BikeProduct> findAllByOrderByProdratingDesc(Pageable pageable);
//  
//  Page<BikeProduct> findAllByOrderByTimesorderedDesc(Pageable pageable);
//  
//  Page<BikeProduct> findAllByOrderByPriceDesc(Pageable pageable);
//  
//  Page<BikeProduct> findByProdnameContainingOrProddescContainingOrderByProdratingDesc(String name , String desc , Pageable pageable);
//  
//  Page<BikeProduct> findByProdnameContainingOrProddescContainingOrderByPriceDesc(String name , String desc , Pageable pageable);
//
//  Page<BikeProduct> findByProdnameContainingOrProddescContainingOrderByPriceAsc(String name , String desc , Pageable pageable);
//  
//  Page<BikeProduct> findByProdnameContainingOrProddescContainingOrderByTimesorderedDesc(String name , String desc , Pageable pageable);
//  
//  Page<BikeProduct> findByProdnameContainingOrProddescContainingOrderByDatetimeAsc(String name , String desc , Pageable pageable);
//  


  
}
