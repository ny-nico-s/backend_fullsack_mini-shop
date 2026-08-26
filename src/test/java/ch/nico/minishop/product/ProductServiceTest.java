package ch.nico.minishop.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import ch.nico.minishop.category.Category;
import ch.nico.minishop.category.CategoryRepository;

@DataJpaTest
@Import(ProductService.class)
class ProductServiceTest {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	private Category drinks;

	@BeforeEach
	void setUp() {
		productRepository.deleteAll();
		categoryRepository.deleteAll();
		drinks = categoryRepository.save(new Category("Getränke"));
	}

	@Test
	void unknownIdThrowsNotFound() {
		ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> productService.findById(999L));

		assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
	}

	@Test
	void updatingUnknownIdThrowsNotFound() {
		Product changed = new Product("Gibt es nicht", new BigDecimal("1.00"), 1, drinks);

		ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> productService.update(999L, changed));

		assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
	}

	@Test
	void createdProductIsReturnedWithId() {
		Product created = productService.create(new Product("Apfelsaft 1L", new BigDecimal("2.50"), 40, drinks));

		assertEquals("Apfelsaft 1L", created.getName());
		assertEquals(new BigDecimal("2.50"), created.getPrice());
		assertTrue(created.getId() > 0);
	}

	@Test
	void deletedProductIsGoneFromTheList() {
		Product created = productService.create(new Product("Salzbrezeln", new BigDecimal("2.10"), 30, drinks));

		productService.delete(created.getId());

		List<Product> remaining = productService.findAll();
		assertEquals(0, remaining.size());
	}

}
