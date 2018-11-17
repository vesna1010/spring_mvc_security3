package test.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;
import com.vesna1010.onlineshop.model.Address;
import com.vesna1010.onlineshop.model.Category;
import com.vesna1010.onlineshop.model.Customer;
import com.vesna1010.onlineshop.model.Product;
import com.vesna1010.onlineshop.repository.CustomerRepository;

@Transactional
public class CustomerRepositoryTest extends BaseRepositoryTest {

	private CustomerRepository repository;
	private Customer customer1;
	private Customer customer2;
	private Customer customer3;
	private Product product1;
	private Product product2;
	private Category category;

	@Before
	public void setUp() {
		repository = context.getBean(CustomerRepository.class);
		
		category = new Category("Category");

		product1 = new Product("1234567890121", "Product A", "Description A", 10.0f, category, getImage(), 10);
		product2 = new Product("1234567890122", "Product B", "Description B", 15.0f, category, getImage(), 8);
		
		customer1 = new Customer("Customer B", "customerB@gmail.com", new Address("Street", "74000", "City", "State"),
				"+387 66 123 123", LocalDate.of(2018, 9, 1));
		customer2 = new Customer("Customer C", "customerC@gmail.com", new Address("Street", "74000", "City", "State"),
				"+387 66 123 122", LocalDate.of(2018, 9, 2));
		customer3 = new Customer("Customer A", "customerA@gmail.com", new Address("Street", "74000", "City", "State"),
				"+387 66 123 121", LocalDate.of(2018, 9, 1));

		Arrays.asList(customer1, customer3).forEach(customer -> customer.addProducts(product1, 2));
		Arrays.asList(customer2, customer3).forEach(customer -> customer.addProducts(product2, 2));

		deleteAllObjects();
		entityManager.persist(category);
		Arrays.asList(product1, product2).forEach(entityManager::persist);
		Arrays.asList(customer1, customer2, customer3).forEach(entityManager::persist);
	}

	@Test
	public void findCustomersByPageOrderByDateDescIdAscTest() {
		List<Customer> page1 = repository.findByPage(1, 2);

		assertThat(page1, hasSize(2));
		assertThat(page1.get(0), is(customer2));
		assertThat(page1.get(1), is(customer1));
	}

	@Test
	public void findCustomersByProductAndPageOrderByDateDescIdAscTest() {
		List<Customer> page1 = repository.findByProductAndPage(product1, 1, 5);

		assertThat(page1, hasSize(2));
		assertThat(page1.get(0), is(customer1));
		assertThat(page1.get(1), is(customer3));
	}

	@Test
	public void saveCustomerTest() {
		Customer customer = new Customer("Customer D", "customerD@gmail.com",
				new Address("Street", "74000", "City", "State"), "+387 66 333 333", LocalDate.of(2018, 9, 7));
		Arrays.asList(product1).forEach(product -> customer.addProducts(product, 2));

		repository.save(customer);

		assertNotNull(customer.getId());
		assertThat(repository.count(), is(4L));
	}

	@Test
	public void countAllCustomersTest() {
		assertTrue(repository.count() == 3);
	}

	@Test
	public void countCustomersByProductTest() {
		assertTrue(repository.countByProduct(product1) == 2);
	}

}
