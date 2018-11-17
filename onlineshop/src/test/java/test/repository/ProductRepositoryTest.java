package test.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;
import com.vesna1010.onlineshop.model.Category;
import com.vesna1010.onlineshop.model.Product;
import com.vesna1010.onlineshop.repository.ProductRepository;

@Transactional
public class ProductRepositoryTest extends BaseRepositoryTest {

	private ProductRepository repository;
	private Product product1;
	private Product product2;
	private Product product3;
	private Category category1;
	private Category category2;

	@Before
	public void setUp() {
		repository = context.getBean(ProductRepository.class);

		category1 = new Category("Category A");
		category2 = new Category("Category B");

		product1 = new Product("1234567890123", "Product A", "Description A", 10.0f, category1, getImage(), 10);
		product2 = new Product("1234567890121", "Product B", "Description B", 15.0f, category2, getImage(), 8);
		product3 = new Product("1234567890122", "Product C", "Description C", 15.0f, category1, getImage(), 8);

		deleteAllObjects();
		Arrays.asList(category1, category2).forEach(entityManager::persist);
		Arrays.asList(product1, product2, product3).forEach(entityManager::persist);
	}

	@Test
	public void findProductsByPageOrderByIdTest() {
		List<Product> page1 = repository.findByPage(1, 2);

		assertThat(page1, hasSize(2));
		assertThat(page1.get(0), is(product2));
		assertThat(page1.get(1), is(product3));
	}

	@Test
	public void findProductsByCategoryAndPageOrderByIdTest() {
		List<Product> page1 = repository.findByCategoryAndPage(category1, 1, 5);

		assertThat(page1, hasSize(2));
		assertThat(page1.get(0), is(product3));
		assertThat(page1.get(1), is(product1));
	}

	@Test
	public void findProductByIdWhenProductExistsTest() {
		Optional<Product> optional = repository.findById("1234567890123");
		Product product = optional.get();

		assertThat(product.getName(), is("Product A"));
		assertThat(product.getCategory(), is(category1));
	}

	@Test
	public void findProductByIdWhenProductNotExistsTest() {
		Optional<Product> optional = repository.findById("1234567890124");

		assertFalse(optional.isPresent());
	}

	@Test
	public void saveProductTest() {
		Product product = new Product("1234567890124", "Product D", "Description D", 15.0f, category1, getImage(), 15);

		product = repository.save(product);

		assertThat(repository.count(), is(4L));
	}

	@Test
	public void updateProductTest() {
		product1.setCategory(category2);

		Product product = repository.save(product1);

		assertThat(product.getName(), is("Product A"));
		assertThat(product.getCategory(), is(category2));
		assertThat(repository.count(), is(3L));
	}

	@Test
	public void deleteProductTest() {
		repository.delete(product1);

		Optional<Product> optional = repository.findById("1234567890123");

		assertFalse(optional.isPresent());
		assertThat(repository.count(), is(2L));
	}

	@Test
	public void countAllProductsTest() {
		assertTrue(repository.count() == 3);
	}

	@Test
	public void countProductsByCategoryTest() {
		assertTrue(repository.countByCategory(category1) == 2);
	}

}
