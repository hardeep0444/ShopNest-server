package com.shopnest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopnest.exception.CartItemException;
import com.shopnest.exception.UserException;
import com.shopnest.modal.CartItem;
import com.shopnest.modal.User;
import com.shopnest.response.ApiResponse;
import com.shopnest.service.CartItemService;
import com.shopnest.service.CartService;
import com.shopnest.service.UserService;

@RestController
@RequestMapping("/api/cart_items")
public class CartItemController {

	private CartItemService cartItemService;
	private UserService userService;
	private CartService cartService;
	
	
	public CartItemController(CartItemService cartItemService,UserService userService, CartService cartService) {
		this.cartItemService=cartItemService;
		this.userService=userService;
		this.cartService=cartService;
	}
	
	@DeleteMapping("/{cartItemId}")
	public ResponseEntity<ApiResponse>deleteCartItemHandler(@PathVariable Long cartItemId, @RequestHeader("Authorization")String jwt) throws CartItemException, UserException{
		
		User user=userService.findUserProfileByJwt(jwt);
		cartItemService.removeCartItem(user.getId(), cartItemId);
		cartService.findUserCart(user.getId());
	
		ApiResponse res=new ApiResponse("Item Removed From Cart",true);
		
		return new ResponseEntity<ApiResponse>(res,HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/{cartItemId}")
	public ResponseEntity<CartItem>updateCartItemHandler(@PathVariable Long cartItemId, @RequestBody CartItem cartItem, @RequestHeader("Authorization")String jwt) throws CartItemException, UserException{
		
		User user=userService.findUserProfileByJwt(jwt);
		
		CartItem updatedCartItem =cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
		
		//ApiResponse res=new ApiResponse("Item Updated",true);
		
		return new ResponseEntity<>(updatedCartItem,HttpStatus.ACCEPTED);
	}
}
