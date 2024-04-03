package com.shopnest.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shopnest.modal.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long>{
	
	@Query("Select r From Rating r where r.product.id=:productId")
	public List<Rating> getAllProductsRating(@Param("productId") Long productId);
}
