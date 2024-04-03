package com.shopnest.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shopnest.modal.Address;
public interface AddressRepository extends JpaRepository<Address, Long> {

}
