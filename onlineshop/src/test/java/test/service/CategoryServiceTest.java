package test.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import com.vesna1010.onlineshop.exception.CategoryCanNotDeleteException;
import com.vesna1010.onlineshop.exception.CategoryNotFoundException;
import com.vesna1010.onlineshop.model.Category;
import com.vesna1010.onlineshop.model.Product;
import com.vesna1010.onlineshop.repository.CategoryRepository;
import com.vesna1010.onlineshop.repository.ProductRepository;
import com.vesna1010.onlineshop.service.CategoryService;
import com.vesna1010.onlineshop.service.impl.CategoryServiceImpl;
import test.stub.StubCategoryRepository;
import test.stub.StubProductRepository;

public class CategoryServiceTest {

	private CategoryService service;
	private CategoryRepository categoryRepository;
	private ProductRepository productRepository;
	private Category category1;
	private Category category2;
	private Category category3;
	private Product product;

	@Before
	public void setUp() {
		categoryRepository = new StubCategoryRepository();
		productRepository = new StubProductRepository();
		service = new CategoryServiceImpl(categoryRepository, productRepository);

		category1 = new Category("Category A");
		category2 = new Category("Category B");
		category3 = new Category("Category C");
		product = new Product("1234567890121", "Product A", "Description A", 55f, category1, new byte[0], 5);

		Arrays.asList(category1, category2, category3).forEach(categoryRepository::save);
		Arrays.asList(product).forEach(productRepository::save);
	}

	@Test
	public void findAllCategoriesTest() {
		List<Category> categories = service.findAllCategories();

		assertThat(categories, hasSize(3));
		assertThat(categories.get(0), is(category1));
		assertThat(categories.get(1), is(category2));
		assertThat(categories.get(2), is(category3));
	}

	@Test
	public void findCategoryByIdWhenCategoryExistsTest() {
		Category category = service.findCategoryById(1L);

		assertThat(category.getName(), is("Category A"));
	}

	@Test(expected = CategoryNotFoundException.class)
	public void findCategoryByIdWhenCategoryNotExistsTest() {
		service.findCategoryById(5L);
	}

	@Test
	public void saveCategoryTest() {
		Category category = new Category("Category");

		category = service.saveCategory(category);

		assertThat(category.getId(), is(4L));
		assertThat(category.getName(), is("Category"));
		assertThat(service.findAllCategories(), hasSize(4));
	}

	@Test
	public void updateCategoryTest() {
		category1.setName("Category");

		Category category = service.saveCategory(category1);

		assertThat(category.getId(), is(1L));
		assertThat(category.getName(), is("Category"));
		assertThat(service.findAllCategories(), hasSize(3));
	}

	@Test
	public void deleteCategoryWhenCategoryProductsIsEmptyTest() {
		service.deleteCategory(category2);

		assertThat(service.findAllCategories(), hasSize(2));
	}

	@Test(expected = CategoryCanNotDeleteException.class)
	public void deleteCategoryWhenCategoryProductsIsNotEmptyTest() {
		service.deleteCategory(category1);
	}

}
