package com.shopnest.service;

import java.util.*;

import com.shopnest.exception.ProductException;
import com.shopnest.modal.Review;
import com.shopnest.modal.User;
import com.shopnest.request.ReviewRequest;

public interface ReviewService {
	
	public Review createReview(ReviewRequest req,User user) throws ProductException;
	
	public List<Review> getAllReview(Long productId);
}
