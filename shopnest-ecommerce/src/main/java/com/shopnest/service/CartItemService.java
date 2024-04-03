package com.shopnest.service;

import com.shopnest.exception.CartItemException;
import com.shopnest.exception.UserException;
import com.shopnest.modal.Cart;
import com.shopnest.modal.CartItem;
import com.shopnest.modal.Product;

public interface CartItemService {
	
	public CartItem createCartItem(CartItem cartItem);
	
	public CartItem updateCartItem(Long userId, Long id,CartItem cartItem) throws CartItemException, UserException;
	
	public CartItem isCartItemExist(Cart cart,Product product,String size, Long userId);
	
	public void removeCartItem(Long userId,Long cartItemId) throws CartItemException, UserException;
	
	public CartItem findCartItemById(Long cartItemId) throws CartItemException;
}
