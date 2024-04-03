package com.shopnest.service;

import org.springframework.stereotype.Service;

import com.shopnest.exception.CartItemException;
import com.shopnest.exception.ProductException;
import com.shopnest.exception.UserException;
import com.shopnest.modal.Cart;
import com.shopnest.modal.CartItem;
import com.shopnest.modal.Product;
import com.shopnest.modal.User;
import com.shopnest.repository.CartRepository;
import com.shopnest.request.AddItemRequest;

@Service
public class CartServiceImplementation implements CartService{
	
	private CartRepository cartRepository;
	private CartItemService cartItemService;
	private ProductService productService;
	
	

	public CartServiceImplementation(CartRepository cartRepository, CartItemService cartItemService,
			ProductService productService) {
		this.cartRepository = cartRepository;
		this.cartItemService = cartItemService;
		this.productService = productService;
	}

	@Override
	public Cart createCart(User user) {
		Cart cart = new Cart();
		cart.setUser(user);
		return cartRepository.save(cart);
	}

	@Override
	public CartItem addCartItem(Long userId, AddItemRequest req) throws ProductException {
		
		Cart cart = cartRepository.findByUserId(userId);
		Product product = productService.findProductById(req.getProductId());
		
		CartItem isPresent = cartItemService.isCartItemExist(cart, product, req.getSize(), userId);
		
		if(isPresent==null)
		{
			CartItem cartItem=new CartItem();
			cartItem.setProduct(product);
			cartItem.setCart(cart);
			cartItem.setQuantity(req.getQuantity());
			cartItem.setUserId(userId);
			
			int price = req.getQuantity()*product.getDiscountedPrice();
			cartItem.setPrice(price);
			cartItem.setSize(req.getSize());
			
			cartItemService.createCartItem(cartItem);
			
		}
		else {
			
				try {
					cartItemService.updateCartItem(userId, isPresent.getId(), isPresent);
				} catch (CartItemException e) {
					
					e.printStackTrace();
				} catch (UserException e) {
					e.printStackTrace();
				}
		}
		findUserCart(userId);
		return isPresent;
		
	}

	@Override
	public Cart findUserCart(Long userId) {
		Cart cart =	cartRepository.findByUserId(userId);
		int totalPrice=0;
		int totalDiscountedPrice=0;
		int totalItem=0;
		for(CartItem cartsItem : cart.getCartItems()) {
			totalPrice+=cartsItem.getPrice();
			totalDiscountedPrice+=cartsItem.getDiscountedPrice();
			totalItem+=cartsItem.getQuantity();
		}
		
		cart.setTotalPrice(totalPrice);
		cart.setTotalItem(cart.getCartItems().size());
		cart.setTotalDiscountedPrice(totalDiscountedPrice);
		cart.setDiscount(totalPrice-totalDiscountedPrice);
		cart.setTotalItem(totalItem);
		
		return cartRepository.save(cart);
	}

}
