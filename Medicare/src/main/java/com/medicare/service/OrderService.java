package com.medicare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medicare.model.Order;
import com.medicare.repo.OrderRepository;

@Service
public class OrderService {
	
	@Autowired
	OrderRepository repo;
	
	public Order addOrder(Order ord) {
		return repo.save(ord);
	}
	
	public List<Order> getOrdersByUserId(int userid) {
		return repo.findAll();
	}
	
	public List<Order> getAllOrders() {
		return repo.findAll();
	}
	
	public Order getOrderById(int id) {
		if(repo.findById(id).isPresent())
			return repo.findById(id).get();
		else
			return null;
	}
	
	public boolean deleteOrder(int id) {
		if (repo.findById(id).isPresent()) {
			repo.deleteById(id);
			return true;
		}
		return false;
	}
}
