package test.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import java.util.Arrays;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.vesna1010.onlineshop.controller.GlobalController;
import com.vesna1010.onlineshop.converter.CategoryConverter;
import com.vesna1010.onlineshop.model.Category;
import com.vesna1010.onlineshop.service.CategoryService;

public abstract class GlobalControllerTest extends BaseControllerTest {

	@Mock
	protected CategoryService categoryService;
	@InjectMocks
	@Autowired
	private GlobalController controller;
	@InjectMocks
	@Autowired
	private CategoryConverter converter;
	protected Category category1;
	protected Category category2;

	{
		category1 = new Category(1L, "Category A");
		category2 = new Category(2L, "Category B");
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

		when(categoryService.findAllCategories()).thenReturn(Arrays.asList(category1, category2));
	}


}
