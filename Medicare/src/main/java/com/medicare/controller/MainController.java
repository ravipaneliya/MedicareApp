package com.medicare.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.medicare.model.User;
import com.medicare.service.CartService;
import com.medicare.service.CategoryService;
import com.medicare.service.OrderService;
import com.medicare.service.ProductService;
import com.medicare.service.UserService;
import com.medicare.model.Cart;
import com.medicare.model.Category;
import com.medicare.model.Order;
import com.medicare.model.Product;

@RestController
public class MainController {

	@Autowired
	UserService userService;
	
	@Autowired
	ProductService prodService;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	CartService cartService;
	
	@Autowired
	OrderService orderService;

//	========================: USER CONTROLLERS : ========================
	@PostMapping("/login")
	public ResponseEntity<Object> authUser(@RequestParam String username,@RequestParam String password) {
		User user = userService.authenticateUser(username, password);
		
		if (user != null && user.getId() > 0)
			return new ResponseEntity<Object>(user, HttpStatus.FOUND);
		else
			return new ResponseEntity<Object>("Invalid Username or Password...!!", HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/user")
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/user/{id}")
	public ResponseEntity<Object> getUser(@PathVariable int id) {
		User user = userService.getUserById(id);

		if (user != null)
			return new ResponseEntity<Object>(user, HttpStatus.FOUND);
		else
			return new ResponseEntity<Object>("User is not available with given id", HttpStatus.NOT_FOUND);
	}

	@PutMapping("/user/{id}")
	public ResponseEntity<Object> updateUser(@PathVariable int id, @RequestBody User user) {
		User updatedUser = userService.updateUser(user, id);

		if (updatedUser != null)
			return new ResponseEntity<Object>(updatedUser, HttpStatus.FOUND);
		else
			return new ResponseEntity<Object>("Updated Operation Failed..", HttpStatus.NOT_FOUND);
	}

	@PostMapping("/user")
	public ResponseEntity<Object> addUser(@RequestBody User user) {
		User addedUser = userService.addUser(user);

		if (addedUser != null)
			return new ResponseEntity<Object>(addedUser, HttpStatus.CREATED);
		else
			return new ResponseEntity<Object>("Error while adding user", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@DeleteMapping("/user/{id}")
	public ResponseEntity<Object> deleteUser(@PathVariable int id) {
		if(userService.deleteUser(id))
			return new ResponseEntity<Object>("User Is Successfully Deleted", HttpStatus.FOUND);
		else
			return new ResponseEntity<Object>("User is not available with given id : " + id, HttpStatus.NOT_FOUND);
	}
	 
//	========================: PRODUCT CONTROLLERS : ========================
	@GetMapping("/admin/product")
	public List<Product> getAllProducts() {
		return prodService.getAllProducts();
	}

	@GetMapping("/admin/product/{id}")
	public ResponseEntity<Object> getProduct(@PathVariable int id) {
		Product prod = prodService.getProductById(id);
		if (prod != null)
			return new ResponseEntity<Object>(prod, HttpStatus.FOUND);
		else
			return new ResponseEntity<Object>("Product is not available with given id", HttpStatus.NOT_FOUND);
	}

	@PostMapping("/admin/product")
	public ResponseEntity<Object> addProduct(@RequestBody Product prod) {
		Category newCategory = getCategoryByObject(prod.getCategory());
		
		prod = new Product(prod.getName(), prod.getCompanyname(), prod.getDescription(), prod.getPrice(), prod.isIsactive(), newCategory);
		Product newProd = prodService.addProduct(prod);
		if (newProd != null)
			return new ResponseEntity<Object>(newProd, HttpStatus.FOUND);
		else
			return new ResponseEntity<Object>("Error while adding Product", HttpStatus.NOT_FOUND);
	}
	
	@PutMapping("/admin/product/{id}")
	public ResponseEntity<Object> updateProduct(@RequestBody Product prod, @PathVariable int id) {
		Product updatedProd = prodService.updateProduct(prod, id);
		if (updatedProd != null)
			return new ResponseEntity<Object>(updatedProd, HttpStatus.FOUND);
		else
			return new ResponseEntity<Object>("Product is not available with given id", HttpStatus.NOT_FOUND);
	}
	
	@DeleteMapping("/admin/product/{id}")
	public ResponseEntity<Object> deleteProduct(@PathVariable int id) {
		if(prodService.deleteProduct(id))
			return new ResponseEntity<Object>("Product Is Successfully Deleted", HttpStatus.FOUND);
		else
			return new ResponseEntity<Object>("Product is not available with given id : " + id, HttpStatus.NOT_FOUND);
	}
//	========================: CATEGORY CONTROLLERS : ========================
	
//	========================: CART CONTROLLERS : ========================

	@PostMapping("/cart")
	public ResponseEntity<Object> addToCart(@RequestBody Cart cart) {
		Cart addedCart = cartService.addCart(new Cart(getUserByObject(cart.getUser()), getProductByObject(cart.getCartItem()), new Date()));
		
		if (addedCart != null)
			return new ResponseEntity<Object>(addedCart, HttpStatus.CREATED);
		else
			return new ResponseEntity<Object>("Error while adding item to Cart", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/cart")
	public List<Cart> getCartsByUserId(@RequestParam int userid){
		return cartService.getCartItemsByUser(userService.getUserById(userid));
	}
	
	@DeleteMapping("/cart/{id}")
	public ResponseEntity<Object> deleteCart(@PathVariable int id) {
		if(cartService.deleteCart(id))
			return new ResponseEntity<Object>("Cart Item Is Successfully Deleted", HttpStatus.FOUND);
		else
			return new ResponseEntity<Object>("Cart Item is not available with given id : " + id, HttpStatus.NOT_FOUND);
	}
//	========================: ORDER CONTROLLERS : ========================

	@PostMapping("/order")
	public ResponseEntity<Object> addOrder(@RequestBody Order order){
		Set<Product> items = new HashSet<Product>();
		double orderTotal = 0;
		for(Product prod : order.getOrderItems()) {
			prod = getProductByObject(prod);
			orderTotal += prod.getPrice();
			items.add(prod);
		}
		
		Order addedOrder = orderService.addOrder(new Order(getUserByObject(order.getUser()), items, new Date(), orderTotal, false));
		
		if (addedOrder != null)
			return new ResponseEntity<Object>(addedOrder, HttpStatus.CREATED);
		else
			return new ResponseEntity<Object>("Error while adding items from Cart to Order.", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/order")
	public List<Order> getOrdersByUserId(@RequestParam int userid){
		return orderService.getOrdersByUserId(userid);
	}

//	========================: OTHER METHODS : ============================
	public Category getCategoryByObject(Category newCategory) {
		Category category = categoryService.getCategoryById(newCategory.getId());
		if (category == null || category.getId() == 0) {
			newCategory = new Category(newCategory.getName(), newCategory.getType());
			return  categoryService.addCategory(newCategory);
		} else {
			return category;
		}
	}
	
	public Product getProductByObject(Product newProduct) {
		Product prod = prodService.getProductById(newProduct.getId());
		if (prod == null || prod.getId() == 0) {
			newProduct = new Product(newProduct.getName(), newProduct.getCompanyname(), 
					newProduct.getDescription(), newProduct.getPrice(), newProduct.isIsactive(), getCategoryByObject(newProduct.getCategory()));
			return prodService.addProduct(newProduct);
		} else {
			return prod;
		}
	}
	
	public User getUserByObject(User newUser) {
		User user = userService.getUserById(newUser.getId());
		if(user != null && user.getId() == 0) {
			newUser = new User(newUser.getName(), newUser.getEmail(), newUser.getPassword(), newUser.getIsadmin(), newUser.getPhoneno());
			return userService.addUser(newUser);
		} else {
			return user;
		}
	}
//	======================================================================
}
