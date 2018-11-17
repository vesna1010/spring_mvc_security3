package test.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;
import com.vesna1010.onlineshop.model.Category;
import com.vesna1010.onlineshop.repository.CategoryRepository;

@Transactional
public class CategoryRepositoryTest extends BaseRepositoryTest {

	private CategoryRepository repository;
	private Category category1;
	private Category category2;
	private Category category3;

	@Before
	public void setUp() {
		repository = context.getBean(CategoryRepository.class);

		category1 = new Category("Category A");
		category2 = new Category("Category B");
		category3 = new Category("Category C");

		deleteAllObjects();
		Arrays.asList(category1, category2, category3).forEach(entityManager::persist);
	}

	@Test
	public void findAllCategoriesOrderByIdTest() {
		List<Category> categories = repository.findAll();

		assertThat(categories, hasSize(3));
		assertThat(categories.get(0), is(category1));
		assertThat(categories.get(1), is(category2));
		assertThat(categories.get(2), is(category3));
	}

	@Test
	public void findCategoryByIdWhenCategoryExistsTest() {
		Long id = category1.getId();
		Optional<Category> optional = repository.findById(id);
		Category category = optional.get();

		assertThat(category.getName(), is("Category A"));
	}

	@Test
	public void findCategoryByIdWhenCategoryNotExistsTest() {
		Long id = category1.getId() + 3;
		Optional<Category> optional = repository.findById(id);

		assertFalse(optional.isPresent());
	}

	@Test
	public void saveCategoryTest() {
		Category category = new Category("Category");

		category = repository.save(category);

		assertNotNull(category.getId());
		assertThat(repository.findAll(), hasSize(4));
	}

	@Test
	public void updateCategoryTest() {
		category1.setName("Category");

		Category category = repository.save(category1);

		assertThat(category.getName(), is("Category"));
		assertThat(repository.findAll(), hasSize(3));
	}

	@Test
	public void deleteCategoryTest() {
		Long id = category1.getId();

		repository.delete(category1);

		Optional<Category> optional = repository.findById(id);

		assertFalse(optional.isPresent());
		assertThat(repository.findAll(), hasSize(2));
	}

}
