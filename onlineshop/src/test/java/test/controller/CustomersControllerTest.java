package test.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.time.LocalDate;
import java.util.Arrays;
import javax.servlet.http.Cookie;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import com.vesna1010.onlineshop.controller.CustomersController;
import com.vesna1010.onlineshop.converter.ProductConverter;
import com.vesna1010.onlineshop.exception.ProductCanNotOrderException;
import com.vesna1010.onlineshop.model.Address;
import com.vesna1010.onlineshop.model.Category;
import com.vesna1010.onlineshop.model.Customer;
import com.vesna1010.onlineshop.model.Product;
import com.vesna1010.onlineshop.service.CustomerService;
import com.vesna1010.onlineshop.service.ProductService;

public class CustomersControllerTest extends GlobalControllerTest {

	@Mock
	private ProductService productService;
	@InjectMocks
	@Autowired
	private ProductConverter converter;
	@Mock
	private CustomerService customerService;
	@InjectMocks
	@Autowired
	private CustomersController controller;
	private Customer customer1;
	private Customer customer2;
	private Product product;

	{	
		customer1 = new Customer("Customer B", "customerB@gmail.com", new Address("Street", "10", "City", "State"),
				"066-111-121", LocalDate.of(2018, 9, 1));
		customer2 = new Customer("Customer C", "customerC@gmail.com", new Address("Street", "10", "City", "State"),
				"066-111-122", LocalDate.of(2018, 9, 2));
		product = new Product("1234567890121", "Product A", "Description A", 10.0f, new Category(1L, "Category"), new byte[0], 10);
		customer1.addProducts(product, 1);
		customer2.addProducts(product, 1);
	}
	
	@Test
	@WithAnonymousUser
	public void renderPageWithCustomersWithAnonymousUserTest() throws Exception {
		when(customerService.findCustomersByPage(1, 10)).thenReturn(Arrays.asList(customer1, customer2));
		when(customerService.countAllCustomers()).thenReturn(2L);
		
		mockMvc.perform(
				get("/customers")
				.param("page", "1")
				.param("size", "10")
				)
		       .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
		       
		verify(customerService, times(0)).findCustomersByPage(1, 10);
		verify(customerService, times(0)).countAllCustomers();
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void renderPageWithCustomersWithAuthenticatedUserTest() throws Exception {
		when(customerService.findCustomersByPage(1, 10)).thenReturn(Arrays.asList(customer1, customer2));
		when(customerService.countAllCustomers()).thenReturn(2L);
		
		mockMvc.perform(
				get("/customers")
				.param("page", "1")
				.param("size", "10")
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("categories", hasSize(2)))
		       .andExpect(model().attribute("customers", hasSize(2)))
		       .andExpect(model().attribute("count", is(2L)))	
		       .andExpect(view().name("customers/page"));
		       
		verify(customerService, times(1)).findCustomersByPage(1, 10);
		verify(customerService, times(1)).countAllCustomers();
		verify(categoryService, times(1)).findAllCategories();
	}
	
	@Test
	@WithAnonymousUser
	public void renderPageWithCustomersByProductWithAnonymousUserTest() throws Exception {
		when(productService.findProductById("1234567890121")).thenReturn(product);
		when(customerService.findCustomersByProductAndPage(product, 1, 10)).thenReturn(Arrays.asList(customer1, customer2));
		when(customerService.countCustomersByProduct(product)).thenReturn(2L);
		
		mockMvc.perform(
				get("/customers")
				.param("page", "1")
				.param("size", "10")
				.param("productId", "1234567890121")
				)
		       .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
		       
		verify(productService, times(0)).findProductById("1234567890121");
		verify(customerService, times(0)).findCustomersByProductAndPage(product, 1, 10);
		verify(customerService, times(0)).countCustomersByProduct(product);
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void renderPageWithCustomersByProductByAuthenticatedUserTest() throws Exception {
		when(productService.findProductById("1234567890121")).thenReturn(product);
		when(customerService.findCustomersByProductAndPage(product, 1, 10)).thenReturn(Arrays.asList(customer1, customer2));
		when(customerService.countCustomersByProduct(product)).thenReturn(2L);
		
		mockMvc.perform(
				get("/customers")
				.param("page", "1")
				.param("size", "10")
				.param("productId", "1234567890121")
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("categories", hasSize(2)))
		       .andExpect(model().attribute("customers", hasSize(2)))
		       .andExpect(model().attribute("count", is(2L)))	
		       .andExpect(view().name("customers/page"));
		
		verify(productService, times(1)).findProductById("1234567890121");
		verify(customerService, times(1)).findCustomersByProductAndPage(product, 1, 10);
		verify(customerService, times(1)).countCustomersByProduct(product);
		verify(categoryService, times(1)).findAllCategories();
	}
	
	@Test
	@WithAnonymousUser
	public void addProductWithAnonymousUserTest() throws Exception {
		mockMvc.perform(
				get("/customers/addProduct")
				.param("productId", "1234567890121")
				.param("numberOfProducts", "0")
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrl("/customers/products"))
		       .andExpect(cookie().value("PRODUCTID1234567890121", "1"))
		       .andExpect(cookie().maxAge("PRODUCTID1234567890121", 2592000));
	}
	
	@Test
	@WithAnonymousUser
	public void removeProductsWithAnonymousUserTest() throws Exception {	
		mockMvc.perform(
				get("/customers/removeProduct")
				.param("productId", "1234567890121")
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(cookie().value("PRODUCTID1234567890121", "0"))
		       .andExpect(cookie().maxAge("PRODUCTID1234567890121", 0));
	}
	
	@Test
	@WithAnonymousUser
	public void renderPageWithSelectedProductsWithAnonymousUserTest() throws Exception {
		when(productService.findProductById("1234567890121")).thenReturn(product);
		
		mockMvc.perform(
				get("/customers/products")
				.with(request -> {
					request.setCookies((new Cookie[] { new Cookie("PRODUCTID1234567890121", "2") }));
					return request;
		        }))
	           .andExpect(status().isOk())
	           .andExpect(model().attribute("categories", hasSize(2)))
		       .andExpect(model().attribute("customer", hasProperty("products", hasEntry(product, 2))))
		       .andExpect(view().name("customers/products"));
		
		verify(productService, times(1)).findProductById("1234567890121");
		verify(categoryService, times(1)).findAllCategories();
	}
	
	@Test
	@WithAnonymousUser
	public void renderEmptyFormWithAnonymousUserTest() throws Exception {
		when(productService.findProductById("1234567890121")).thenReturn(product);
		
		mockMvc.perform(
				get("/customers/form")
				.with(request -> {
					request.setCookies((new Cookie[] { new Cookie("PRODUCTID1234567890121", "2") }));
					return request;
		        }))
	           .andExpect(status().isOk())
	           .andExpect(model().attribute("categories", hasSize(2)))
		       .andExpect(model().attribute("customer", hasProperty("products", hasEntry(product, 2))))
		       .andExpect(view().name("customers/form"));
		
		verify(productService, times(1)).findProductById("1234567890121");
		verify(categoryService, times(1)).findAllCategories();
	}
	
	@Test
	@WithAnonymousUser
	public void saveCustomerValidFormWithAnonymousUserTest() throws Exception {
		Customer customer = new Customer("Customer", "customer@gmail.com", new Address("Street", "74000", "City", "State"),
				"+387 66 111 111", LocalDate.of(2018, 9, 1));
		customer.addProducts(product, 2);
		
		when(productService.findProductById("1234567890121")).thenReturn(product);
		doNothing().when(customerService).saveCustomer(customer);
		
		mockMvc.perform(
				post("/customers/save")
				.with(csrf())
				.param("fullName", "Customer")
				.param("email", "customer@gmail.com")
				.param("address.street", "Street")
				.param("address.zipCode", "74000")
				.param("address.city", "City")
				.param("address.state", "State")
				.param("telephone", "+387 66 111 111")
				.param("date", "01.09.2018")
				.param("products[1234567890121]", "2"))
		       .andExpect(model().attribute("message", is("These products are ordered!")))
		       .andExpect(model().attribute("categories", hasSize(2)))
	           .andExpect(view().name("customers/products"));
		
		verify(productService, times(1)).findProductById("1234567890121");
		verify(customerService, times(1)).saveCustomer(customer);
		verify(categoryService, times(1)).findAllCategories();
	}
	
	@Test
	@WithAnonymousUser
	public void canNotSaveCustomerValidFormWithAnonymousUserTest() throws Exception {
		Customer customer = new Customer("Customer", "customer@gmail.com", new Address("Street", "74000", "City", "State"),
				"+387 66 111 111", LocalDate.of(2018, 9, 1));
		customer.addProducts(product, 2);
		
		when(productService.findProductById("1234567890121")).thenReturn(product);
		doThrow(new ProductCanNotOrderException("We do not have enough stock of the product: Product A!")).when(customerService)
				.saveCustomer(customer);
	
		mockMvc.perform(
				post("/customers/save")
				.with(csrf())
				.param("fullName", "Customer")
				.param("email", "customer@gmail.com")
				.param("address.street", "Street")
				.param("address.zipCode", "74000")
				.param("address.city", "City")
				.param("address.state", "State")
				.param("telephone", "+387 66 111 111")
				.param("date", "01.09.2018")
				.param("products[1234567890121]", "2"))
		       .andExpect(model().attribute("message", is("We do not have enough stock of the product: Product A!")))
		       .andExpect(model().attribute("categories", hasSize(2)))
	           .andExpect(view().name("customers/products"));
		
		verify(productService, times(1)).findProductById("1234567890121");
		verify(customerService, times(1)).saveCustomer(customer);
		verify(categoryService, times(1)).findAllCategories();
	}
	
	@Test
	@WithAnonymousUser
	public void saveCustomerInvalidFormWithAnonymousUserTest() throws Exception {
		Customer customer = new Customer("Customer", "customer@gmail.com", new Address("Street", "74000", "City", "State"),
				"+387 66 111 111", LocalDate.of(2018, 9, 1));
		
		doNothing().when(customerService).saveCustomer(customer);
		
		mockMvc.perform(
				post("/customers/save")
				.with(csrf())
				.param("fullName", "Customer")
				.param("email", "customer@gmail.com")
				.param("address.street", "Street")
				.param("address.zipCode", "74000")
				.param("address.city", "City")
				.param("address.state", "State")
				.param("telephone", "+387 66 111 111")
				.param("date", "01.09.2018"))
		       .andExpect(status().isOk())
		       .andExpect(model().attributeHasFieldErrors("customer", "products"))
		       .andExpect(model().attribute("categories", hasSize(2)))
		       .andExpect(view().name("customers/form"));
		
		verify(customerService, times(0)).saveCustomer(customer);
		verify(categoryService, times(1)).findAllCategories();
	}
	
}
