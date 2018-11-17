package test.stub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import com.vesna1010.onlineshop.model.User;
import com.vesna1010.onlineshop.repository.UserRepository;

public class StubUserRepository implements UserRepository {

	private Map<String, User> map = new HashMap<>();

	@Override
	public List<User> findByPage(Integer currentPage, Integer pageSize) {
		return map
				.values()
				.stream()
				.sorted((u1, u2) -> u1.getUsername().compareTo(u2.getUsername()))
				.skip((currentPage - 1) * pageSize)
				.limit(pageSize)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return (map.containsKey(username)) ? Optional.of(map.get(username)) : Optional.empty();
	}

	@Override
	public User save(User user) {
		String username = user.getUsername();

		map.put(username, user);

		return map.get(username);
	}

	@Override
	public void disableUser(User user) {
		String username = user.getUsername();

		user.setEnabled(false);

		map.put(username, user);
	}

	@Override
	public void deleteUser(User user) {
		String username = user.getUsername();

		map.remove(username, user);
	}

	@Override
	public long count() {
		return map.size();
	}

}
