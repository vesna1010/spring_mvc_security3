package test.stub;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.vesna1010.onlineshop.model.Customer;
import com.vesna1010.onlineshop.model.Product;
import com.vesna1010.onlineshop.repository.CustomerRepository;

public class StubCustomerRepository implements CustomerRepository {

	Map<Long, Customer> map = new HashMap<>();

	@Override
	public List<Customer> findByPage(Integer currentPage, Integer pageSize) {
		return map
				.values()
				.stream()
				.sorted(new CustomerComparator())
				.skip((currentPage - 1) * pageSize)
				.limit(pageSize)
				.collect(Collectors.toList());
	}

	@Override
	public List<Customer> findByProductAndPage(Product product, Integer currentPage, Integer pageSize) {
		return map
				.values()
				.stream()
				.filter(customer -> customer.getProducts().keySet().contains(product))
				.sorted(new CustomerComparator())
				.skip((currentPage - 1) * pageSize)
				.limit(pageSize)
				.collect(Collectors.toList());
	}

	@Override
	public void save(Customer customer) {
		Long id = getGeneratedId();

		customer.setId(id);
		map.put(id, customer);
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
	public long count() {
		return map.size();
	}

	@Override
	public long countByProduct(Product product) {
		return map
				.values()
				.stream()
				.filter(customer -> customer.getProducts().keySet().contains(product))
				.count();
	}

	private class CustomerComparator implements Comparator<Customer> {

		@Override
		public int compare(Customer c1, Customer c2) {
			Comparator<Customer> comparator = Comparator.comparing(customer -> customer.getDate());

			comparator = comparator.reversed();
			comparator = comparator.thenComparing(customer -> customer.getId());

			return comparator.compare(c1, c2);
		}
	}

}
