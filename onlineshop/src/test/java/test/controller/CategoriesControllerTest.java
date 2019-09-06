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
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import com.vesna1010.onlineshop.controller.CategoriesController;
import com.vesna1010.onlineshop.exception.CategoryCanNotDeleteException;
import com.vesna1010.onlineshop.exception.CategoryNotFoundException;
import com.vesna1010.onlineshop.model.Category;

public class CategoriesControllerTest extends GlobalControllerTest {

	@InjectMocks
	@Autowired
	private CategoriesController controller;
	
	@Test
	@WithAnonymousUser
	public void renderPageWithCategoriesWithAnonymousUserTest() throws Exception {
		mockMvc.perform(get("/categories"))
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void renderPageWithCategoriesWithAuthenticatedUserTest() throws Exception {
		mockMvc.perform(get("/categories"))
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("categories", hasSize(2)))
	               .andExpect(view().name("categories/page"));
		
		verify(categoryService, times(1)).findAllCategories();
	}
	
	@Test
	@WithAnonymousUser
	public void renderCategoryFormWithAnonymousUserTest() throws Exception {
		mockMvc.perform(get("/categories/form"))
		       .andExpect(status().is3xxRedirection())
                       .andExpect(redirectedUrlPattern("**/login"));
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void renderCategoryFormWithAuthenticatedUserTest() throws Exception {
		mockMvc.perform(get("/categories/form"))
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("categories", hasSize(2)))
		       .andExpect(model().attribute("category", is(new Category())))
		       .andExpect(view().name("categories/form"));
		
		verify(categoryService, times(1)).findAllCategories();
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void saveCategoryAndRenderFormValidFormTest() throws Exception {
		Category category = new Category(3L, "Category");
		
		when(categoryService.saveCategory(category)).thenReturn(category);
		
		mockMvc.perform(
				post("/categories/save")
				.param("id", "3")
				.param("name", "Category")
				.with(csrf())
				)
		       .andExpect(model().hasNoErrors())
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrl("/categories/form"));
		
		verify(categoryService, times(1)).saveCategory(category);
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void saveCategoryAndRenderFormInvalidFormTestTest() throws Exception {
		Category category = new Category(3L, "Category 1");
		
		when(categoryService.saveCategory(category)).thenReturn(category);
		
		mockMvc.perform(
				post("/categories/save")
				.param("id", "3")
				.param("name", "Category 1")
				.with(csrf()))
		       .andExpect(status().isOk())
		       .andExpect(model().attributeHasFieldErrors("category", "name"))
		       .andExpect(model().attribute("category", is(category)))
		       .andExpect(model().attribute("categories", hasSize(2)))
		       .andExpect(view().name("categories/form"));
		
		verify(categoryService, times(0)).saveCategory(category);
		verify(categoryService, times(1)).findAllCategories();
	}
	
	@Test
	@WithAnonymousUser
	public void renderFormWithCategoryWithAnonymousUserTest() throws Exception {
		when(categoryService.findCategoryById(1L)).thenReturn(category1);

		mockMvc.perform(
				get("/categories/edit")
				.param("categoryId", "1")
				)
		       .andExpect(status().is3xxRedirection())
                       .andExpect(redirectedUrlPattern("**/login"));

		verify(categoryService, times(0)).findCategoryById(1L);
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void renderFormWithCategoryWhenCategoryExistsByAuthenticateUserTest() throws Exception {
		when(categoryService.findCategoryById(1L)).thenReturn(category1);

		mockMvc.perform(
				get("/categories/edit")
				.param("categoryId", "1")
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("categories", hasSize(2)))
		       .andExpect(model().attribute("category", hasProperty("name", is("Category A"))))
		       .andExpect(view().name("categories/form"));

		verify(categoryService, times(1)).findCategoryById(1L);
		verify(categoryService, times(1)).findAllCategories();
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void renderFormWithCategoryWhenCategoryNotExistsByAuthenticateUserTest() throws Exception {
		when(categoryService.findCategoryById(1L)).thenThrow(
				new CategoryNotFoundException("Not Found Category By Id 1"));

		mockMvc.perform(
				get("/categories/edit")
				.param("categoryId", "1")
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("message", is("Not Found Category By Id 1")))
		       .andExpect(view().name("exceptions/page"));

		verify(categoryService, times(1)).findCategoryById(1L);
	}
	
	@Test
	@WithAnonymousUser
	public void deleteCategoryWithAnonymousUserTest() throws Exception {
		when(categoryService.findCategoryById(1L)).thenReturn(category1);
		doNothing().when(categoryService).deleteCategory(category1);
		
		mockMvc.perform(
				get("/categories/delete")
				.param("categoryId", "1")
				)
		       .andExpect(status().is3xxRedirection())
                       .andExpect(redirectedUrlPattern("**/login"));
		
		verify(categoryService, times(0)).findCategoryById(1L);
		verify(categoryService, times(0)).deleteCategory(category1);
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void deleteCategoryWhenCategoryExistsWithAuthenticateUserTest() throws Exception {
		when(categoryService.findCategoryById(1L)).thenReturn(category1);
		doNothing().when(categoryService).deleteCategory(category1);
		
		mockMvc.perform(
				get("/categories/delete")
				.param("categoryId", "1")
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrl("/categories"));
		
		verify(categoryService, times(1)).findCategoryById(1L);
		verify(categoryService, times(1)).deleteCategory(category1);
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void deleteCategoryWhenCategoryNotExistsWithAuthenticateUserTest() throws Exception {
		doThrow(new CategoryNotFoundException("Not Found Category By Id 3")).when(
				categoryService).findCategoryById(3L);
				
		mockMvc.perform(
				get("/categories/delete")
				.param("categoryId", "3")
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("message", is("Not Found Category By Id 3")))
		       .andExpect(view().name("exceptions/page"));
		
		verify(categoryService, times(1)).findCategoryById(3L);
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void deleteCategoryWhenCategoryHasProductsWithAuthenticateUserTest() throws Exception {
		when(categoryService.findCategoryById(1L)).thenReturn(category1);
		doThrow(new CategoryCanNotDeleteException("Can Not Delete Category By Id 1")).when(
				categoryService).deleteCategory(category1);
		
		mockMvc.perform(
				get("/categories/delete")
				.param("categoryId", "1")
				)
		       .andExpect(model().attribute("message", is("Can Not Delete Category By Id 1")))
	               .andExpect(view().name("exceptions/page"));
		
		verify(categoryService, times(1)).findCategoryById(1L);
		verify(categoryService, times(1)).deleteCategory(category1);
	}
	
}
