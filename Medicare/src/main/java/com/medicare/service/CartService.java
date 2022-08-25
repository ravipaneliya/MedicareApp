package com.medicare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medicare.model.Cart;
import com.medicare.model.User;
import com.medicare.repo.CartRepository;

@Service
public class CartService {

	@Autowired
	CartRepository repo;
	
	public Cart addCart(Cart cart) {
		return repo.save(cart);
	}
	
	public List<Cart> getAllCart() {
		return repo.findAll();
	}
	
	public List<Cart> getCartItemsByUser(User user) {
		return repo.getCartByUser(user);
	}
	
	public Cart getCartById(int id) {
		if(repo.findById(id).isPresent())
			return repo.findById(id).get();
		else
			return null;
	}
	
	public boolean deleteCart(int id) {
		if (repo.findById(id).isPresent()) {
			repo.deleteById(id);
			return true;
		}
		return false;
	}
}
