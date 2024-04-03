package com.shopnest.service;

import com.shopnest.exception.ProductException;
import com.shopnest.modal.Cart;
import com.shopnest.modal.CartItem;
import com.shopnest.modal.User;
import com.shopnest.request.AddItemRequest;

public interface CartService {
	
	public Cart createCart(User user);
	
	public CartItem addCartItem(Long userId,AddItemRequest req) throws ProductException;
	
	public Cart findUserCart(Long userId);
}
