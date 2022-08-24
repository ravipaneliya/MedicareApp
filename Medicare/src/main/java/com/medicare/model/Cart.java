package com.medicare.model;

import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "carts")
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "product_id")
	private Set<Product> cartItems;
	
	private Date cartDate;
	private double cartTotal;
	
	public Cart() {
	}

	public Cart(User user, Set<Product> cartItems, Date cartDate, double cartTotal) {
		super();
		this.user = user;
		this.cartItems = cartItems;
		this.cartDate = cartDate;
		this.cartTotal = cartTotal;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Product> getCartItems() {
		return cartItems;
	}

	public void setCartItems(Set<Product> cartItems) {
		this.cartItems = cartItems;
	}

	public Date getCartDate() {
		return cartDate;
	}

	public void setCartDate(Date cartDate) {
		this.cartDate = cartDate;
	}

	public double getCartTotal() {
		return cartTotal;
	}

	public void setCartTotal(double cartTotal) {
		this.cartTotal = cartTotal;
	}
}
