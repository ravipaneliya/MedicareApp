package com.medicare.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.medicare.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{

}
