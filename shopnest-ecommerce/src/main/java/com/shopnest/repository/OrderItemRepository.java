package com.shopnest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopnest.modal.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
