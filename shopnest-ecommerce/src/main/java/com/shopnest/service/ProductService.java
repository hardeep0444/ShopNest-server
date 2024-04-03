package com.shopnest.service;
import java.util.*;

import org.springframework.data.domain.Page;

import com.shopnest.exception.ProductException;
import com.shopnest.modal.Product;
import com.shopnest.request.CreateProductRequest;

public interface ProductService {
	public Product createProduct(CreateProductRequest req);
	
	public String deleteProduct(Long productId) throws ProductException;
	
	public Product updateProduct(Long productId, Product req) throws ProductException;
	
	public Product findProductById(Long id) throws ProductException;
	
	public List<Product> findProductByCategory(String category);
	
	public Page<Product> getAllProduct(String category, List<String>colors, List<String> sizes, Integer minPrice, Integer maxPrice, Integer minDiscount,String sort, String stock, Integer pageNumber, Integer pageSize);
	
	
	
}
