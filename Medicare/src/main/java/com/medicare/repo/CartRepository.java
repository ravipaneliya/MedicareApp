package com.medicare.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.medicare.model.Cart;
import com.medicare.model.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer>{

	@Query("SELECT c FROM Cart c WHERE user = :user ") 
	public List<Cart> getCartByUser(@Param("user") User user);
}
