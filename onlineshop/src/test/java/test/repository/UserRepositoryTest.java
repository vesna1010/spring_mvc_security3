package test.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import com.vesna1010.onlineshop.enums.Authority;
import com.vesna1010.onlineshop.model.User;
import com.vesna1010.onlineshop.repository.UserRepository;

@Transactional
public class UserRepositoryTest extends BaseRepositoryTest {

	private UserRepository repository;
	private PasswordEncoder encoder;
	private User user1;
	private User user2;
	private User user3;

	@Before
	public void setUp() {
		repository = context.getBean(UserRepository.class);
		encoder = context.getBean(PasswordEncoder.class);

		user1 = new User("UsernameB", encoder.encode("PasswordB"), "emailB@gmail.com", Authority.USER);
		user2 = new User("UsernameC", encoder.encode("PasswordC"), "emailC@gmail.com", Authority.USER);
		user3 = new User("UsernameA", encoder.encode("PasswordA"), "emailA@gmail.com", Authority.ADMIN);

		deleteAllObjects();
		Arrays.asList(user1, user2, user3).forEach(entityManager::persist);
	}

	@Test
	public void findUsersByPageOrderByUsernameTest() {
		List<User> page1 = repository.findByPage(1, 2);

		assertThat(page1, hasSize(2));
		assertThat(page1.get(0), is(user3));
		assertThat(page1.get(1), is(user1));
	}

	@Test
	public void findUserByUsernameWhenUserExistsTest() {
		Optional<User> optional = repository.findByUsername("UsernameA");
		User user = optional.get();

		assertThat(user.getEmail(), is("emailA@gmail.com"));
		assertThat(user.getAuthority(), is(Authority.ADMIN));
		assertTrue(user.isEnabled());
	}

	@Test
	public void findUserByUsernameWhenUserNotExistsTest() {
		Optional<User> optional = repository.findByUsername("UsernameD");

		assertFalse(optional.isPresent());
	}

	@Test
	public void saveUserTest() {
		User user = new User("UsernameD", encoder.encode("PasswordD"), "emailD@gmail.com", Authority.USER);

		user = repository.save(user);

		assertThat(user.getUsername(), is("UsernameD"));
		assertThat(repository.count(), is(4L));
	}

	@Test
	public void updateUserTest() {
		String password = encoder.encode("NewPassword");

		user3.setPassword(password);

		User user = repository.save(user3);

		assertThat(user.getPassword(), is(password));
		assertThat(repository.count(), is(3L));
	}

	@Test
	public void disableUserTest() {
		repository.disableUser(user1);

		Optional<User> optional = repository.findByUsername("UsernameB");
		User user = optional.get();

		assertFalse(user.isEnabled());
	}

	@Test
	public void deleteUserTest() {
		repository.deleteUser(user1);

		Optional<User> optional = repository.findByUsername("UsernameB");

		assertFalse(optional.isPresent());
	}

	@Test
	public void countUsersTest() {
		assertTrue(repository.count() == 3);
	}

}
