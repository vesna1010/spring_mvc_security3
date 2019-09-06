package test.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
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
import com.vesna1010.onlineshop.controller.UsersController;
import com.vesna1010.onlineshop.converter.UserConverter;
import com.vesna1010.onlineshop.enums.Authority;
import com.vesna1010.onlineshop.exception.UserNotFoundException;
import com.vesna1010.onlineshop.model.User;
import com.vesna1010.onlineshop.service.UserService;

public class UsersControllerTest extends GlobalControllerTest {

	@Mock
	private UserService userService;
	@InjectMocks
	@Autowired
	private UserConverter converter;
	@InjectMocks
	@Autowired
	private UsersController controller;
	private User user1;
	private User user2;
	
	{
		user1 = new User("UsernameA", "PasswordA", "emailA@gmail.com", Authority.USER);
		user2 = new User("UsernameB", "PasswordB", "emailB@gmail.com", Authority.ADMIN);
	}
	
	@Test
	@WithAnonymousUser
	public void renderPageWithUsersWithAnonymousUserTest() throws Exception {
		when(userService.findUsersByPage(1, 10)).thenReturn(Arrays.asList(user1, user2));
		when(userService.countAllUsers()).thenReturn(2L);
		
		mockMvc.perform(
				get("/users")
				.param("page","1")
				.param("size", "10")
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));
		
		
		verify(userService, times(0)).findUsersByPage(1, 10);
		verify(userService, times(0)).countAllUsers();
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void renderPageWithUsersWithAuthorityUserTest() throws Exception {
		when(userService.findUsersByPage(1, 10)).thenReturn(Arrays.asList(user1, user2));
		when(userService.countAllUsers()).thenReturn(2L);
		
		mockMvc.perform(
				get("/users")
				.param("page","1")
				.param("size", "10")
				)
		       .andExpect(status().isForbidden())
		       .andExpect(forwardedUrl("/denied"));
		
		verify(userService, times(0)).findUsersByPage(1, 10);
		verify(userService, times(0)).countAllUsers();
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void renderPageWithUsersWithAuthorityAdminTest() throws Exception {
		when(userService.findUsersByPage(1, 10)).thenReturn(Arrays.asList(user1, user2));
		when(userService.countAllUsers()).thenReturn(2L);
		
		mockMvc.perform(
				get("/users")
				.param("page","1")
				.param("size", "10")
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("categories", hasSize(2)))
		       .andExpect(model().attribute("users", hasSize(2)))
		       .andExpect(model().attribute("count", is(2L)))
		       .andExpect(view().name("users/page"));
		
		verify(userService, times(1)).findUsersByPage(1, 10);
		verify(userService, times(1)).countAllUsers();
		verify(categoryService, times(1)).findAllCategories();
	}
	
	@Test
	@WithAnonymousUser
	public void renderUserFormWithAnonymousUserTest() throws Exception {
		mockMvc.perform(get("/users/form"))
		       .andExpect(status().is3xxRedirection())
	               .andExpect(redirectedUrlPattern("**/login"));
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void renderUserFormWithAuthorityUserTest() throws Exception {
		mockMvc.perform(get("/users/form"))
		       .andExpect(status().isForbidden())
	               .andExpect(forwardedUrl("/denied"));
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void renderUserFormWithAuthoriyAdminTest() throws Exception {
		mockMvc.perform(get("/users/form"))
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("categories", hasSize(2)))
		       .andExpect(model().attribute("user", is(new User())))
		       .andExpect(view().name("users/form"));
		
		verify(categoryService, times(1)).findAllCategories();
	}
	
	@Test
	@WithMockUser(username = "Username", authorities = "USER")
	public void saveUserWithAuthenticatedUserValidFormTest() throws Exception {
		User user = new User("Username", "Password", "email@gmail.com", Authority.ADMIN);
		
		when(userService.saveUser(user)).thenReturn(user);
		
		mockMvc.perform(
				post("/users/save")
				.param("username", "Username")
				.param("password", "Password")
				.param("confirmPassword", "Password")
				.param("email", "email@gmail.com")
				.param("authority", "ADMIN")
				.param("enabled", "true")
				.with(csrf())
				)
		       .andExpect(status().isOk())
		       .andExpect(model().hasNoErrors())
		       .andExpect(model().attribute("categories", hasSize(2)))
		       .andExpect(model().attribute("user", hasProperty("username", is("Username"))))
		       .andExpect(model().attribute("user", hasProperty("email", is("email@gmail.com"))))
		       .andExpect(view().name("users/form"));
		
		verify(userService, times(1)).saveUser(user);
		verify(categoryService, times(1)).findAllCategories();
	}
	
	@Test
	@WithMockUser(username = "Username", authorities = "USER")
	public void saveUserWithAuthenticatedUserInvalidFormTest() throws Exception {
		User user = new User("Username", "Password", "invalidEmail", Authority.ADMIN);
		
		when(userService.saveUser(user)).thenReturn(user);
		
		mockMvc.perform(
				post("/users/save")
				.param("username", "Username")
				.param("password", "Password")
				.param("confirmPassword", "")
				.param("email", "invalidEmail")
				.param("authority", "ADMIN")
				.param("enabled", "true")
				.with(csrf())
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attributeHasFieldErrors("user", "email", "confirmPassword"))
		       .andExpect(model().attribute("categories", hasSize(2)))
		       .andExpect(model().attribute("user", hasProperty("username", is("Username"))))
		       .andExpect(model().attribute("user", hasProperty("email", is("invalidEmail"))))
		       .andExpect(view().name("users/form"));
		
		verify(userService, times(0)).saveUser(user);
		verify(categoryService, times(1)).findAllCategories();
	}
	
	@Test
	@WithAnonymousUser
	public void renderFormWithUserWithAnonymousTest() throws Exception {
		mockMvc.perform(get("/users/edit"))
		       .andExpect(status().is3xxRedirection())
                       .andExpect(redirectedUrlPattern("**/login"));
	}
	
	@Test
	@WithMockUser(username = "Username", password = "Password", authorities = "USER")
	public void renderFormWithUserWithAuthenticateUserTest() throws Exception {
		when(userService.findUserByUsername("Username")).thenReturn(
				new User("Username", "Password", "email@gmail.com", Authority.USER));
		
		mockMvc.perform(get("/users/edit"))
		       .andExpect(model().attribute("categories", hasSize(2)))
		       .andExpect(model().attribute("user", hasProperty("email", is("email@gmail.com"))))
		       .andExpect(model().attribute("user", hasProperty("authorities", hasSize(1))))
		       .andExpect(view().name("users/form"));
		
		verify(userService, times(1)).findUserByUsername("Username");
		verify(categoryService, times(1)).findAllCategories();
	}
	
	@Test
	@WithAnonymousUser
	public void deleteUserWithAnonymousUserTest() throws Exception {
		when(userService.findUserByUsername("UsernameA")).thenReturn(user1);
		doNothing().when(userService).deleteUser(user1);
		
		mockMvc.perform(
				get("/users/delete")
				.param("username", "UsernameA")
				)
		       .andExpect(status().is3xxRedirection())
                       .andExpect(redirectedUrlPattern("**/login"));
		
		verify(userService, times(0)).findUserByUsername("UsernameA");
		verify(userService, times(0)).deleteUser(user1);
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void deleteUserWithAuthorityUserTest() throws Exception {
		when(userService.findUserByUsername("UsernameA")).thenReturn(user1);
		doNothing().when(userService).deleteUser(user1);
		
		mockMvc.perform(
				get("/users/delete")
				.param("username", "UsernameA")
				)
	    	       .andExpect(status().isForbidden())
                       .andExpect(forwardedUrl("/denied"));
		
		verify(userService, times(0)).findUserByUsername("UsernameA");
		verify(userService, times(0)).deleteUser(user1);
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void deleteUserWhenUserExistsWithAuthorityAdminTest() throws Exception {
		when(userService.findUserByUsername("UsernameA")).thenReturn(user1);
		doNothing().when(userService).deleteUser(user1);
		
		mockMvc.perform(
				get("/users/delete")
				.param("username", "UsernameA")
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrl("/users"));
		
		verify(userService, times(1)).findUserByUsername("UsernameA");
		verify(userService, times(1)).deleteUser(user1);
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void deleteUserWhenUserNotExistsWithAuthorityAdminTest() throws Exception {
		doThrow(new UserNotFoundException("Not Found User By Username UsernameA")).when(
				userService).findUserByUsername("UsernameA");
		
		mockMvc.perform(
				get("/users/delete")
				.param("username", "UsernameA")
				)
		       .andExpect(status().isOk())
	               .andExpect(model().attribute("message", is("Not Found User By Username UsernameA")))
	               .andExpect(view().name("exceptions/page"));
		
		verify(userService, times(1)).findUserByUsername("UsernameA");
	}
	
	@Test
	@WithAnonymousUser
	public void disableUserWithAnonymousUserTest() throws Exception {
		when(userService.findUserByUsername("UsernameA")).thenReturn(user1);
		doNothing().when(userService).disableUser(user1);
		
		mockMvc.perform(
				get("/users/disable")
				.param("username", "UsernameA")
				)
		       .andExpect(status().is3xxRedirection())
                       .andExpect(redirectedUrlPattern("**/login"));
		
		verify(userService, times(0)).findUserByUsername("UsernameA");
		verify(userService, times(0)).disableUser(user1);
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void disableUserWithAuthorityUserTest() throws Exception {
		when(userService.findUserByUsername("UsernameA")).thenReturn(user1);
		doNothing().when(userService).disableUser(user1);
		
		mockMvc.perform(
				get("/users/disable")
				.param("username", "UsernameA")
				)
		       .andExpect(status().isForbidden())
                       .andExpect(forwardedUrl("/denied"));
		
		verify(userService, times(0)).findUserByUsername("UsernameA");
		verify(userService, times(0)).disableUser(user1);
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void disableUserWhenUserExistsWithAuthorityAdminTest() throws Exception {
		when(userService.findUserByUsername("UsernameA")).thenReturn(user1);
		doNothing().when(userService).disableUser(user1);
		
		mockMvc.perform(
				get("/users/disable")
				.param("username", "UsernameA")
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrl("/users"));
		
		verify(userService, times(1)).findUserByUsername("UsernameA");
		verify(userService, times(1)).disableUser(user1);
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void disableUserWhenUserNotExistsWithAuthorityAdminTest() throws Exception {
		doThrow(new UserNotFoundException("Not Found User By Username UsernameA")).when(
				userService).findUserByUsername("UsernameA");
		
		mockMvc.perform(
				get("/users/disable")
				.param("username", "UsernameA")
				)
		       .andExpect(status().isOk())
	               .andExpect(model().attribute("message", is("Not Found User By Username UsernameA")))
	               .andExpect(view().name("exceptions/page"));
		
		verify(userService, times(1)).findUserByUsername("UsernameA");
	}
	
}
