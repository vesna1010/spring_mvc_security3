package test.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

import com.vesna1010.onlineshop.exception.ProductCanNotOrderException;
import com.vesna1010.onlineshop.model.Address;
import com.vesna1010.onlineshop.model.Category;
import com.vesna1010.onlineshop.model.Customer;
import com.vesna1010.onlineshop.model.Product;
import com.vesna1010.onlineshop.repository.CustomerRepository;
import com.vesna1010.onlineshop.repository.ProductRepository;
import com.vesna1010.onlineshop.service.CustomerService;
import com.vesna1010.onlineshop.service.impl.CustomerServiceImpl;
import test.stub.StubCustomerRepository;
import test.stub.StubProductRepository;

public class CustomerServiceTest {

	private CustomerService service;
	private CustomerRepository customerRepository;
	private ProductRepository productRepository;
	private Customer customer1;
	private Customer customer2;
	private Customer customer3;
	private Product product1;
	private Product product2;

	@Before
	public void setUp() {
		customerRepository = new StubCustomerRepository();
		productRepository = new StubProductRepository();
		service = new CustomerServiceImpl(customerRepository, productRepository);

		product1 = new Product("1234567890121", "Product A", "Description A", 10.0f, new Category(1L, "Category"),
				new byte[0], 10);
		product2 = new Product("1234567890122", "Product B", "Description B", 15.0f, new Category(1L, "Category"),
				new byte[0], 10);

		customer1 = new Customer("Customer B", "customerB@gmail.com", new Address("Street", "10", "City", "State"),
				"066-111-121", LocalDate.of(2018, 9, 1));
		customer2 = new Customer("Customer C", "customerC@gmail.com", new Address("Street", "10", "City", "State"),
				"066-111-122", LocalDate.of(2018, 9, 2));
		customer3 = new Customer("Customer A", "customerA@gmail.com", new Address("Street", "10", "City", "State"),
				"066-111-123", LocalDate.of(2018, 9, 1));

		Arrays.asList(customer1, customer3).forEach(customer -> customer.addProducts(product1, 2));
		Arrays.asList(customer2, customer3).forEach(customer -> customer.addProducts(product2, 2));

		Arrays.asList(product1, product2).forEach(productRepository::save);
		Arrays.asList(customer1, customer2, customer3).forEach(customerRepository::save);
	}

	@Test
	public void findCustomersByPageTest() {
		List<Customer> page1 = service.findCustomersByPage(1, 2);

		assertThat(page1, hasSize(2));
		assertThat(page1.get(0), is(customer2));
		assertThat(page1.get(1), is(customer1));
	}

	@Test
	public void findCustomersByProductAndPageTest() {
		List<Customer> page1 = service.findCustomersByProductAndPage(product1, 1, 5);

		assertThat(page1, hasSize(2));
		assertThat(page1.get(0), is(customer1));
		assertThat(page1.get(1), is(customer3));
	}

	@Test
	public void saveCustomerTest() {
		Customer customer = new Customer("Customer D", "customerD@gmail.com",
				new Address("Street", "10", "City", "State"), "066-111-125", LocalDate.of(2018, 9, 7));
		customer.addProducts(product1, 2);

		service.saveCustomer(customer);
		Optional<Product> optional = productRepository.findById("1234567890121");
		Product product = optional.get();

		assertThat(product.getStocks(), is(8));
		assertThat(customer.getId(), is(4L));
		assertThat(service.countAllCustomers(), is(4L));
	}
	
	@Test(expected = ProductCanNotOrderException.class)
	public void canNotSaveCustomerTest() {
		Customer customer = new Customer("Customer D", "customerD@gmail.com",
				new Address("Street", "10", "City", "State"), "066-111-125", LocalDate.of(2018, 9, 7));
		customer.addProducts(product1, 11);

		service.saveCustomer(customer);
	}

	@Test
	public void countAllCustomersTest() {
		assertThat(service.countAllCustomers(), is(3L));
	}

	@Test
	public void countCustomersByProductTest() {
		assertThat(service.countCustomersByProduct(product1), is(2L));
	}
}
