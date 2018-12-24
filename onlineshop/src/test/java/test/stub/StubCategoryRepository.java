package test.stub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import com.vesna1010.onlineshop.model.Category;
import com.vesna1010.onlineshop.repository.CategoryRepository;

public class StubCategoryRepository implements CategoryRepository {

	private Map<Long, Category> map = new HashMap<>();

	@Override
	public List<Category> findAll() {
		return map
				.values()
				.stream()
				.sorted((c1, c2) -> c1.getId().compareTo(c2.getId()))
				.collect(Collectors.toList());
	}

	@Override
	public Optional<Category> findById(Long id) {
		return Optional.ofNullable(map.get(id));
	}

	@Override
	public Category save(Category category) {
		Long id = category.getId();

		if (id == null) {
			id = getGeneratedId();
			category.setId(id);
		}

		map.put(id, category);

		return map.get(id);
	}

	private Long getGeneratedId() {
		return map
				.keySet()
				.stream()
				.mapToLong(a -> a)
				.max()
				.orElse(0) + 1;
	}

	@Override
	public void delete(Category category) {
		map.remove(category.getId(), category);
	}

}
