package com.medicare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.medicare.model.User;
import com.medicare.repo.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository repo;

	public User authenticateUser(String username, String password) {
		User user = repo.getUserByUsernameAndPasword(username, password);
		if (user == null) {
            user = new User();
        }
		return user;
	}

	public User addUser(User user) {
		user = repo.save(user);
		if (user != null) {
			user = getUserById(user.getId());
		}
		return user;
	}

	public List<User> getAllUsers() {
		return repo.findAll();
	}

	public User getUserById(int id) {
		if (repo.findById(id).isPresent())
			return repo.findById(id).get();
		else
			return null;
	}

	public User updateUser(User user, int id) {
		if (repo.findById(id).isPresent()) {
			User old = repo.findById(id).get();
			old.setName(user.getName());
			old.setEmail(user.getEmail());
			old.setPassword(user.getPassword());
			old.setIsadmin(user.getIsadmin());
			old.setPhoneno(user.getPhoneno());
			return repo.save(old);
		} else
			return null;
	}
	
	public boolean deleteUser(int id) {
		if (repo.findById(id).isPresent()) {
			repo.deleteById(id);
			return true;
		}
		return false;
	}
}
