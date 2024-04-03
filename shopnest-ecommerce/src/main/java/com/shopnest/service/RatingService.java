package com.shopnest.service;

import java.util.*;

import com.shopnest.exception.ProductException;
import com.shopnest.modal.Rating;
import com.shopnest.modal.User;
import com.shopnest.request.RatingRequest;

public interface RatingService {
	
	public Rating createRating(RatingRequest req,User user) throws ProductException;
	
	public List<Rating> getProductsRating(Long productId);

}
