package com.medicare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.medicare.model.Category;
import com.medicare.repo.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	CategoryRepository categoryRepo;

	public Category addCategory(Category cat) {
		return categoryRepo.save(cat);
	}

	public Category getCategoryById(int id) {
		if (categoryRepo.findById(id).isPresent())
			return categoryRepo.findById(id).get();
		else
			return null;
	}
}
