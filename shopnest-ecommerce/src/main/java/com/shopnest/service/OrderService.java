package com.shopnest.service;

import java.util.*;

import com.shopnest.exception.OrderException;
import com.shopnest.modal.Address;
import com.shopnest.modal.Order;
import com.shopnest.modal.User;

public interface OrderService {
	
	public Order createOrder(User user, Address shippingAdress);
	
	public Order findOrderById(Long orderId) throws OrderException;
	
	public List<Order> usersOrderHistory(Long userId);
	
	public Order placedOrder(Long orderId) throws OrderException;
	
	public Order confirmedOrder(Long orderId)throws OrderException;
	
	public Order shippedOrder(Long orderId) throws OrderException;
	
	public Order deliveredOrder(Long orderId) throws OrderException;
	
	public Order cancledOrder(Long orderId) throws OrderException;
	
	public List<Order>getAllOrders();
	
	public void deleteOrder(Long orderId) throws OrderException;
}
