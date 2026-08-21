package ch.nico.minishop.product;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public List<Product> findAll() {
		return productRepository.findAll();
	}

	public List<Product> findByCategory(Long categoryId) {
		return productRepository.findByCategoryId(categoryId);
	}

	public Product findById(Long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produkt nicht gefunden"));
	}

	public Product create(Product product) {
		return productRepository.save(product);
	}

	public Product update(Long id, Product product) {
		Product existing = findById(id);
		existing.setName(product.getName());
		existing.setPrice(product.getPrice());
		existing.setStock(product.getStock());
		existing.setCategory(product.getCategory());
		return productRepository.save(existing);
	}

	public void delete(Long id) {
		Product existing = findById(id);
		productRepository.delete(existing);
	}

}
