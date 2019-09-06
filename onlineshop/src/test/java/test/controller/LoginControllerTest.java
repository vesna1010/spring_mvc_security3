package test.controller;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.junit.Test;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

public class LoginControllerTest extends GlobalControllerTest {
	
	@Test
	@WithAnonymousUser
	public void renderLoginFormWithAnonymousUserTest() throws Exception {
		mockMvc.perform(get("/login"))
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("categories", hasSize(2)))
		       .andExpect(view().name("login/form"));
		
		verify(categoryService, times(1)).findAllCategories();
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void renderLoginFormWithAuthenticatedUserTest() throws Exception {
		mockMvc.perform(get("/login"))
		       .andExpect(status().isForbidden())
	               .andExpect(forwardedUrl("/denied"));
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void renderDeniedPageTest() throws Exception {
		mockMvc.perform(get("/denied"))
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("categories", hasSize(2)))
		       .andExpect(view().name("exceptions/page"));
		
		verify(categoryService, times(1)).findAllCategories();
	}
	
}
