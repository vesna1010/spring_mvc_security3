package test.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.vesna1010.onlineshop.enums.Authority;
import com.vesna1010.onlineshop.exception.UserNotFoundException;
import com.vesna1010.onlineshop.model.User;
import com.vesna1010.onlineshop.repository.UserRepository;
import com.vesna1010.onlineshop.service.UserService;
import com.vesna1010.onlineshop.service.impl.UserServiceImpl;
import test.stub.StubUserRepository;

public class UserServiceTest {

	private UserService service;
	private UserRepository repository;
	private PasswordEncoder encoder;
	private User user1;
	private User user2;
	private User user3;

	@Before
	public void setUp() {
		repository = new StubUserRepository();
		encoder = new BCryptPasswordEncoder();
		service = new UserServiceImpl(repository, encoder);

		user1 = new User("UsernameB", "PasswordB", "emailB@gmail.com", Authority.ADMIN);
		user2 = new User("UsernameC", "PasswordC", "emailC@gmail.com", Authority.USER);
		user3 = new User("UsernameA", "PasswordA", "emailA@gmail.com", Authority.ADMIN);

		Arrays.asList(user1, user2, user3).forEach(repository::save);
	}

	@Test
	public void findUsersByPageTest() {
		List<User> page1 = service.findUsersByPage(1, 2);

		assertThat(page1, hasSize(2));
		assertThat(page1.get(0), is(user3));
		assertThat(page1.get(1), is(user1));
	}

	@Test
	public void findUserByUsernameWhenUserExistsTest() {
		User user = service.findUserByUsername("UsernameA");

		assertThat(user.getAuthority(), is(Authority.ADMIN));
		assertThat(user.getEmail(), is("emailA@gmail.com"));
	}

	@Test(expected = UserNotFoundException.class)
	public void findUserByUsernameWhenUserNotExistsTest() {
		service.findUserByUsername("UsernameE");
	}

	@Test
	public void saveUserTest() {
		User user = new User("UsernameE", "PasswordE", "emailE@gmail.com", Authority.USER);

		user = service.saveUser(user);

		assertThat(user.getEmail(), is("emailE@gmail.com"));
		assertThat(service.countAllUsers(), is(4L));
	}

	@Test
	public void updateUserTest() {
		user1.setAuthority(Authority.USER);

		User user = service.saveUser(user1);

		assertThat(user.getAuthority(), is(Authority.USER));
		assertThat(service.countAllUsers(), is(3L));
	}

	@Test
	public void disableUserTest() {
		service.disableUser(user3);

		User user = service.findUserByUsername("UsernameA");

		assertFalse(user.isEnabled());
	}

	@Test
	public void deleteUserTest() {
		service.deleteUser(user3);

		assertThat(service.countAllUsers(), is(2L));
	}

	@Test
	public void countAllUsersTest() {
		assertThat(service.countAllUsers(), is(3L));
	}

}
