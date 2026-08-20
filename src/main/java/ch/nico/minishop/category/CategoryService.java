package ch.nico.minishop.category;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	public Category create(Category category) {
		return categoryRepository.save(category);
	}

}
