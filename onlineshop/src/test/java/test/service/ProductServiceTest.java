package test.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import com.vesna1010.onlineshop.exception.ProductCanNotDeleteException;
import com.vesna1010.onlineshop.exception.ProductNotFoundException;
import com.vesna1010.onlineshop.model.Address;
import com.vesna1010.onlineshop.model.Category;
import com.vesna1010.onlineshop.model.Customer;
import com.vesna1010.onlineshop.model.Product;
import com.vesna1010.onlineshop.repository.CustomerRepository;
import com.vesna1010.onlineshop.repository.ProductRepository;
import com.vesna1010.onlineshop.service.ProductService;
import com.vesna1010.onlineshop.service.impl.ProductServiceImpl;
import test.stub.StubCustomerRepository;
import test.stub.StubProductRepository;

public class ProductServiceTest {

	private ProductService service;
	private ProductRepository productRepository;
	private CustomerRepository customerRepository;
	private Product product1;
	private Product product2;
	private Product product3;
	private Category category1;
	private Category category2;
	private Customer customer;

	@Before
	public void setUp() {
		productRepository = new StubProductRepository();
		customerRepository = new StubCustomerRepository();
		service = new ProductServiceImpl(productRepository, customerRepository);

		category1 = new Category(1L, "Category A");
		category2 = new Category(2L, "Category B");

		product1 = new Product("1234567890123", "Product A", "Description A", 10.0f, category1, new byte[0], 10);
		product2 = new Product("1234567890121", "Product B", "Description B", 15.0f, category2, new byte[0], 8);
		product3 = new Product("1234567890122", "Product C", "Description C", 15.0f, category1, new byte[0], 8);

		customer = new Customer("Customer", "customer@gmail.com", new Address("Street", "74000", "City", "State"),
				"+387 66 111 121", LocalDate.of(2018, 9, 1));
		customer.addProducts(product2, 2);

		Arrays.asList(product1, product2, product3).forEach(productRepository::save);
		Arrays.asList(customer).forEach(customerRepository::save);
	}

	@Test
	public void findProductsByPageTest() {
		List<Product> page1 = service.findProductsByPage(1, 2);

		assertThat(page1, hasSize(2));
		assertThat(page1.get(0), is(product2));
		assertThat(page1.get(1), is(product3));
	}

	@Test
	public void findProductsByCategoryAndPageTest() {
		List<Product> page1 = service.findProductsByCategoryAndPage(category1, 1, 5);

		assertThat(page1, hasSize(2));
		assertThat(page1.get(0), is(product3));
		assertThat(page1.get(1), is(product1));
	}

	@Test
	public void findProductByIdWhenProductExistsTest() {
		Product product = service.findProductById("1234567890123");

		assertThat(product.getName(), is("Product A"));
		assertThat(product.getCategory(), is(category1));
	}

	@Test(expected = ProductNotFoundException.class)
	public void findProductByIdWhenProductNotExistsTest() {
		service.findProductById("1234567890124");
	}

	@Test
	public void saveProductTest() {
		Product product = new Product("1234567890124", "Product D", "Description D", 55f, category1, new byte[0], 1);

		product = service.saveProduct(product);

		assertThat(product.getName(), is("Product D"));
		assertThat(service.countAllProducts(), is(4L));
	}

	@Test
	public void updateProductTest() {
		product1.setCategory(category2);

		Product product = service.saveProduct(product1);

		assertThat(product.getName(), is("Product A"));
		assertThat(product.getCategory(), is(category2));
		assertThat(service.countAllProducts(), is(3L));
	}

	@Test
	public void deleteProductByIdWhenProductCustomersIsEmptyTest() {
		service.deleteProduct(product1);

		assertThat(service.countAllProducts(), is(2L));
	}

	@Test(expected = ProductCanNotDeleteException.class)
	public void deleteProductByIdWhenProductCustomersIsNotEmptyTes() {
		service.deleteProduct(product2);
	}

	@Test
	public void countAllProductsTest() {
		assertThat(service.countAllProducts(), is(3L));
	}

	@Test
	public void countProductsByCategoryTest() {
		assertThat(service.countProductsByCategory(category1), is(2L));
	}

}
