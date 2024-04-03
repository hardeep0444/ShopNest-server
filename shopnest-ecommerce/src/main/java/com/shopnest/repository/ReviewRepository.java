package com.shopnest.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shopnest.modal.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{
	
	@Query("Select r from Review r where r.product.id=:productId")
	public List<Review> getAllProductsReview(@Param("productId") Long productId);
}
