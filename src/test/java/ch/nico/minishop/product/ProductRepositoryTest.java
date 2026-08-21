package ch.nico.minishop.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import ch.nico.minishop.category.Category;
import ch.nico.minishop.category.CategoryRepository;

@DataJpaTest
class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	private Category drinks;
	private Category snacks;

	@BeforeEach
	void setUp() {
		productRepository.deleteAll();
		categoryRepository.deleteAll();
		drinks = categoryRepository.save(new Category("Getränke"));
		snacks = categoryRepository.save(new Category("Snacks"));
		productRepository.save(new Product("Apfelsaft 1L", new BigDecimal("2.50"), 40, drinks));
		productRepository.save(new Product("Mineralwasser 1.5L", new BigDecimal("1.20"), 80, drinks));
		productRepository.save(new Product("Schokoriegel", new BigDecimal("1.50"), 100, snacks));
	}

	@Test
	void savedProductIsFoundAgain() {
		Product saved = productRepository.save(new Product("Eistee Pfirsich 0.5L", new BigDecimal("1.80"), 60, drinks));

		Optional<Product> found = productRepository.findById(saved.getId());

		assertTrue(found.isPresent());
		assertEquals("Eistee Pfirsich 0.5L", found.get().getName());
		assertEquals(new BigDecimal("1.80"), found.get().getPrice());
		assertEquals(60, found.get().getStock());
	}

	@Test
	void filterByCategoryReturnsOnlyProductsOfThatCategory() {
		List<Product> drinkProducts = productRepository.findByCategoryId(drinks.getId());
		List<Product> snackProducts = productRepository.findByCategoryId(snacks.getId());

		assertEquals(2, drinkProducts.size());
		assertEquals(1, snackProducts.size());
		assertEquals("Schokoriegel", snackProducts.get(0).getName());
	}

	@Test
	void unknownIdIsNotFound() {
		Optional<Product> found = productRepository.findById(999L);

		assertTrue(found.isEmpty());
	}

}
