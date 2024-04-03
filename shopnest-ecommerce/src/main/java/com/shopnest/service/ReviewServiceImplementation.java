package com.shopnest.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shopnest.exception.ProductException;
import com.shopnest.modal.Product;
import com.shopnest.modal.Review;
import com.shopnest.modal.User;
import com.shopnest.repository.ProductRepository;
import com.shopnest.repository.ReviewRepository;
import com.shopnest.request.ReviewRequest;

@Service
public class ReviewServiceImplementation implements ReviewService {
	
	private ReviewRepository reviewRepository;
	private ProductService productService;
	private ProductRepository productRepository;
	
	public ReviewServiceImplementation(ReviewRepository reviewRepository,ProductService productService,ProductRepository productRepository) {
		this.reviewRepository=reviewRepository;
		this.productService=productService;
		this.productRepository=productRepository;
	}
	
	@Override
	public Review createReview(ReviewRequest req, User user) throws ProductException {
		Product product=productService.findProductById(req.getProductId());
		Review review=new Review();
		review.setUser(user);
		review.setProduct(product);
		review.setReview(req.getReview());
		review.setCreatedAt(LocalDateTime.now());
		
//		product.getReviews().add(review);
//		productRepository.save(product);
		return reviewRepository.save(review);
	}

	@Override
	public List<Review> getAllReview(Long productId) {
		return reviewRepository.getAllProductsReview(productId);
	}

}
