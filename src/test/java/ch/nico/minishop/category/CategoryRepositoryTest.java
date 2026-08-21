package ch.nico.minishop.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository categoryRepository;

	@BeforeEach
	void setUp() {
		categoryRepository.deleteAll();
	}

	@Test
	void savedCategoryIsFoundAgain() {
		Category saved = categoryRepository.save(new Category("Getränke"));

		Optional<Category> found = categoryRepository.findById(saved.getId());

		assertTrue(found.isPresent());
		assertEquals("Getränke", found.get().getName());
	}

	@Test
	void allSavedCategoriesAreListed() {
		categoryRepository.save(new Category("Getränke"));
		categoryRepository.save(new Category("Snacks"));

		assertEquals(2, categoryRepository.findAll().size());
	}

}
