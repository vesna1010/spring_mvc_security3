package test.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.util.Arrays;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import com.vesna1010.onlineshop.controller.ProductsController;
import com.vesna1010.onlineshop.converter.ProductConverter;
import com.vesna1010.onlineshop.exception.ProductCanNotDeleteException;
import com.vesna1010.onlineshop.exception.ProductNotFoundException;
import com.vesna1010.onlineshop.model.Product;
import com.vesna1010.onlineshop.service.ProductService;

public class ProductsControllerTest extends GlobalControllerTest {

	@Mock
	private ProductService productService;
	@InjectMocks
	@Autowired
	private ProductConverter converter;
	@InjectMocks
	@Autowired
	private ProductsController controller;
	private Product product1;
	private Product product2;
	
	{
		product1 = new Product("1234567890121", "Product A", "Description A", 55f, category1, new byte[0], 5);
		product2 = new Product("1234567890122", "Product B", "Description B", 45f, category1, new byte[0], 5);
	}
	
	@Test
	@WithAnonymousUser
	public void renderPageWithProductsWithAnonymousUserTest() throws Exception {
		when(productService.findProductsByPage(1, 10)).thenReturn(Arrays.asList(product1, product2));
		when(productService.countAllProducts()).thenReturn(2L);
		
		mockMvc.perform(
				get("/products")
				.param("page", "1")
				.param("size", "10")
				)
		       .andExpect(status().is3xxRedirection())
	               .andExpect(redirectedUrlPattern("**/login"));
		
		verify(productService, times(0)).findProductsByPage(1, 10);
		verify(productService, times(0)).countAllProducts();
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void renderPageWithProductsWithAuthenticatedUserTest() throws Exception {
		when(productService.findProductsByPage(1, 10)).thenReturn(Arrays.asList(product1, product2));
		when(productService.countAllProducts()).thenReturn(2L);
		
		mockMvc.perform(
				get("/products")
				.param("page", "1")
				.param("size", "10")
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("categories", hasSize(2)))
		       .andExpect(model().attribute("products", hasSize(2)))
		       .andExpect(model().attribute("count", is(2L)))
		       .andExpect(view().name("products/page"));
		
		verify(productService, times(1)).findProductsByPage(1, 10);
		verify(productService, times(1)).countAllProducts();
		verify(categoryService, times(1)).findAllCategories();
	}
	
	@Test
	@WithAnonymousUser
	public void renderPageWithProductsByCategoryWithAnonymousUserTest() throws Exception {
		when(categoryService.findCategoryById(1L)).thenReturn(category1);
		when(productService.findProductsByCategoryAndPage(category1, 1, 10)).thenReturn(Arrays.asList(product1, product2));
		when(productService.countProductsByCategory(category1)).thenReturn(2L);
		
		mockMvc.perform(
				get("/products")
				.param("categoryId", "1")
				.param("page", "1")
				.param("size", "10")
				)
		       .andExpect(status().is3xxRedirection())
                       .andExpect(redirectedUrlPattern("**/login"));
		
		verify(categoryService, times(0)).findCategoryById(1L);
		verify(productService, times(0)).findProductsByCategoryAndPage(category1, 1, 10);
		verify(productService, times(0)).countProductsByCategory(category1);
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void renderPageWithProductsByCategoryWithAuthenticatedUserTest() throws Exception {
		when(categoryService.findCategoryById(1L)).thenReturn(category1);
		when(productService.findProductsByCategoryAndPage(category1, 1, 10)).thenReturn(Arrays.asList(product1, product2));
		when(productService.countProductsByCategory(category1)).thenReturn(2L);
		
		mockMvc.perform(
				get("/products")
				.param("categoryId", "1")
				.param("page", "1")
				.param("size", "10")
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("categories", hasSize(2)))
		       .andExpect(model().attribute("products", hasSize(2)))
		       .andExpect(model().attribute("count", is(2L)))
		       .andExpect(view().name("products/page"));
		
		verify(categoryService, times(1)).findCategoryById(1L);
		verify(productService, times(1)).findProductsByCategoryAndPage(category1, 1, 10);
		verify(productService, times(1)).countProductsByCategory(category1);
		verify(categoryService, times(1)).findAllCategories();
	}

	@Test
	@WithAnonymousUser
	public void renderEmptyFormWithAnonymousUserTest() throws Exception {
		mockMvc.perform(get("/products/form"))
		       .andExpect(status().is3xxRedirection())
                       .andExpect(redirectedUrlPattern("**/login"));
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void renderEmptyFormWithAuthenticatedUserTest() throws Exception {
		mockMvc.perform(get("/products/form"))
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("categories", hasSize(2)))
		       .andExpect(model().attribute("product", is(new Product())))
		       .andExpect(view().name("products/form"));
		
		verify(categoryService, times(1)).findAllCategories();
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void saveProductWithAuthenticatedUserValidFormTest() throws Exception {
		Product product = new Product("1234567890121", "Product", "Description", 55.00f, category1,
				new byte[0], 5);
		
		when(categoryService.findCategoryById(1L)).thenReturn(category1);
		when(productService.saveProduct(product)).thenReturn(product);
		
		mockMvc.perform(
				post("/products/save")
				.param("id", "1234567890121")
				.param("name", "Product")
				.param("description", "Description")
				.param("price", "55.00")
				.param("category", "1")
				.param("image", Arrays.toString(new byte[0]))
				.param("stocks", "5")
				.with(csrf())
				)
		       .andExpect(model().hasNoErrors())
	               .andExpect(status().is3xxRedirection())
	               .andExpect(redirectedUrl("/products/form"));
		
		verify(categoryService, times(1)).findCategoryById(1L);
		verify(productService, times(1)).saveProduct(product);
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void saveProductWithAuthenticatedUserInvalidFormTest() throws Exception {
		Product product = new Product("1234567890121", "Product ??", "Description", 55.00f, category1,
				new byte[0], 5);
		
		when(categoryService.findCategoryById(1L)).thenReturn(category1);
		when(productService.saveProduct(product)).thenReturn(product);
		
		mockMvc.perform(
				post("/products/save")
				.param("id", "1234567890121")
				.param("name", "Product ??")
				.param("description", "Description")
				.param("price", "55.00")
				.param("category", "1")
				.param("image", Arrays.toString(new byte[0]))
				.param("stocks", "5")
				.with(csrf())
				)
		       .andExpect(status().isOk())
	               .andExpect(model().attributeHasFieldErrors("product", "name"))
	               .andExpect(model().attribute("product", hasProperty("name", is("Product ??"))))
	               .andExpect(model().attribute("categories", hasSize(2)))
	               .andExpect(view().name("products/form"));
		
		verify(categoryService, times(1)).findCategoryById(1L);
		verify(productService, times(0)).saveProduct(product);
		verify(categoryService, times(1)).findAllCategories();
	}
	
	@Test
	@WithAnonymousUser
	public void renderFormWithProductWithAnonymousUserTest() throws Exception {
		when(productService.findProductById("1234567890121")).thenReturn(product1);
		
		mockMvc.perform(
				get("/products/edit")
				.param("productId", "1234567890121")
				)
		       .andExpect(status().is3xxRedirection())
                       .andExpect(redirectedUrlPattern("**/login"));
		
		verify(productService, times(0)).findProductById("1234567890121");
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void renderFormWithProductWhenProductExistsWithAuthenticatedUserTest() throws Exception {
		when(productService.findProductById("1234567890121")).thenReturn(product1);
		
		mockMvc.perform(
				get("/products/edit")
				.param("productId", "1234567890121")
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("product", is(product1)))
	               .andExpect(model().attribute("categories", hasSize(2)))
	               .andExpect(view().name("products/form"));
		
		verify(productService, times(1)).findProductById("1234567890121");
		verify(categoryService, times(1)).findAllCategories();
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void renderFormWithProductWhenProductNotExistsWithAuthenticatedUserTest() throws Exception {
		when(productService.findProductById("1234567890121")).thenThrow(
				new ProductNotFoundException("Not Found Product By Id 1234567890121"));
		
		mockMvc.perform(
				get("/products/edit")
				.param("productId", "1234567890121")
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("message", is("Not Found Product By Id 1234567890121")))
		       .andExpect(view().name("exceptions/page"));
		
		verify(productService, times(1)).findProductById("1234567890121");
	}
	
	@Test
	@WithAnonymousUser
	public void renderPageWithProductDetailsWhenProductExistsWithAnonymousUserTest() throws Exception {
		when(productService.findProductById("1234567890121")).thenReturn(product1);
		
		mockMvc.perform(
				get("/products/details")
				.param("productId", "1234567890121")
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("product", is(product1)))
	               .andExpect(model().attribute("categories", hasSize(2)))
	               .andExpect(view().name("products/details"));
		
		verify(productService, times(1)).findProductById("1234567890121");
		verify(categoryService, times(1)).findAllCategories();
	}
	
	@Test
	@WithAnonymousUser
	public void renderPageWithProductDetailsWhenProductNotExistsWithAnonymousUserTest() throws Exception {
		when(productService.findProductById("1234567890121")).thenThrow(
				new ProductNotFoundException("Not Found Product By Id 1234567890121"));
		
		mockMvc.perform(
				get("/products/details")
				.param("productId", "1234567890121")
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("message", is("Not Found Product By Id 1234567890121")))
		       .andExpect(view().name("exceptions/page"));
		
		verify(productService, times(1)).findProductById("1234567890121");
	}
	
	@Test
	@WithAnonymousUser
	public void deleteProductWithAnonymousUserTest() throws Exception {
		when(productService.findProductById("1234567890121")).thenReturn(product1);
		doNothing().when(productService).deleteProduct(product1);
		
		mockMvc.perform(
				get("/products/delete")
				.param("productId", "1234567890121")
				)
		       .andExpect(status().is3xxRedirection())
                       .andExpect(redirectedUrlPattern("**/login"));
		
		verify(productService, times(0)).findProductById("1234567890121");
		verify(productService, times(0)).deleteProduct(product1);
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void deleteProductWhenProductExistsWithAuthenticateUserTest() throws Exception {
		when(productService.findProductById("1234567890121")).thenReturn(product1);
		doNothing().when(productService).deleteProduct(product1);
		
		mockMvc.perform(
				get("/products/delete")
				.param("productId", "1234567890121")
				)
	       	       .andExpect(status().is3xxRedirection())
	               .andExpect(redirectedUrl("/products"));
		
		verify(productService, times(1)).findProductById("1234567890121");
		verify(productService, times(1)).deleteProduct(product1);
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void deleteProductWhenProductNotExistsWithAuthenticateUserTest() throws Exception {
		doThrow(new ProductNotFoundException("Not Found Product By Id 1234567890121")).when(
				productService).findProductById("1234567890123");
		
		mockMvc.perform(
				get("/products/delete")
				.param("productId", "1234567890123")
				)
		       .andExpect(status().isOk())
	               .andExpect(model().attribute("message", is("Not Found Product By Id 1234567890121")))
	               .andExpect(view().name("exceptions/page"));
		
		verify(productService, times(1)).findProductById("1234567890123");
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void deleteProductWhenProductHasCustomersWithAuthenticateUserTest() throws Exception {
		when(productService.findProductById("1234567890121")).thenReturn(product1);
		doThrow(new ProductCanNotDeleteException("Can Not Delete Product By Id 1234567890121")).when(
				productService).deleteProduct(product1);
		
		mockMvc.perform(
				get("/products/delete")
				.param("productId", "1234567890121")
				)
		       .andExpect(status().isOk())
	               .andExpect(model().attribute("message", is("Can Not Delete Product By Id 1234567890121")))
	               .andExpect(view().name("exceptions/page"));
		
		verify(productService, times(1)).findProductById("1234567890121");
		verify(productService, times(1)).deleteProduct(product1);
	}
	
}
