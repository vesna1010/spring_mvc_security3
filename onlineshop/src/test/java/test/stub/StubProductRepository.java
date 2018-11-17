package test.stub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import com.vesna1010.onlineshop.model.Category;
import com.vesna1010.onlineshop.model.Product;
import com.vesna1010.onlineshop.repository.ProductRepository;

public class StubProductRepository implements ProductRepository {

	private Map<String, Product> map = new HashMap<>();

	@Override
	public List<Product> findByPage(Integer currentPage, Integer pageSize) {
		return map
				.values()
				.stream()
				.sorted((p1, p2) -> p1.getId().compareTo(p2.getId()))
				.skip((currentPage - 1) * pageSize)
				.limit(pageSize)
				.collect(Collectors.toList());
	}

	@Override
	public List<Product> findByCategoryAndPage(Category category, Integer currentPage, Integer pageSize) {
		return map
				.values()
				.stream()
				.filter(product -> product.getCategory().equals(category))
				.sorted((p1, p2) -> p1.getId().compareTo(p2.getId()))
				.skip((currentPage - 1) * pageSize)
				.limit(pageSize)
				.collect(Collectors.toList());
	}

	@Override
	public Product save(Product product) {
		String id = product.getId();
		
		map.put(id, product);

		return map.get(id);
	}

	@Override
	public void delete(Product product) {
		map.remove(product.getId(), product);
	}

	@Override
	public Optional<Product> findById(String id) {
		return (map.containsKey(id)) ? Optional.of(map.get(id)) : Optional.empty();
	}

	@Override
	public long count() {
		return map.size();
	}

	@Override
	public long countByCategory(Category category) {
		return map
				.values()
				.stream()
				.filter(product -> product.getCategory().equals(category))
				.count();
	}

}
