package test.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.util.Arrays;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import com.vesna1010.onlineshop.controller.HomeController;
import com.vesna1010.onlineshop.model.Product;
import com.vesna1010.onlineshop.service.ProductService;

public class HomeControllerTest extends GlobalControllerTest {
	
	@Mock
	private ProductService productService;
	@InjectMocks
	@Autowired
	private HomeController homeController;
	private Product product1;
	private Product product2;
	
	{
		product1 = new Product("1234567890123", "Product A", "Description A", 10.0f, category1, new byte[0], 10);
		product2 = new Product("1234567890121", "Product B", "Description B", 15.0f, category1, new byte[0], 8);
	}
	
	@Test
	@WithAnonymousUser
	public void redirectHomePageWithProductsWithAnonymousUserTest() throws Exception {
		when(productService.findProductsByPage(1, 10)).thenReturn(Arrays.asList(product1, product2));
		when(productService.countAllProducts()).thenReturn(2L);
		
		mockMvc.perform(
				get("/")
				.param("page", "1")
				.param("size", "10")
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("categories", hasSize(2)))
		       .andExpect(model().attribute("products", hasSize(2)))
		       .andExpect(model().attribute("count", is(2L)))
		       .andExpect(view().name("home"));
		
		verify(productService, times(1)).findProductsByPage(1, 10);
		verify(productService, times(1)).countAllProducts();
		verify(categoryService, times(1)).findAllCategories();
	}
	
	@Test
	@WithAnonymousUser
	public void redirectHomePageWithProductsByCategoryWithAnonymousUserTest() throws Exception {
		when(categoryService.findCategoryById(1L)).thenReturn(category1);
		when(productService.findProductsByCategoryAndPage(category1, 1, 10)).thenReturn(Arrays.asList(product1, product2));
		when(productService.countProductsByCategory(category1)).thenReturn(2L);
		
		mockMvc.perform(
				get("/")
				.param("categoryId", "1")
				.param("page", "1")
				.param("size", "10")
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("categories", hasSize(2)))
		       .andExpect(model().attribute("products", hasSize(2)))
		       .andExpect(model().attribute("count", is(2L)))
		       .andExpect(view().name("home"));
		
		verify(categoryService, times(1)).findCategoryById(1L);
		verify(productService, times(1)).findProductsByCategoryAndPage(category1, 1, 10);
		verify(productService, times(1)).countProductsByCategory(category1);
		verify(categoryService, times(1)).findAllCategories();
	}
	
}
