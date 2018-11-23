package test.controller;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/resources/spring/beans-dao.xml",
		"file:src/main/resources/spring/beans-security.xml",
		"file:src/main/resources/spring/dispatcherServlet/beans-web.xml" })
@WebAppConfiguration
public abstract class BaseControllerTest {

	@Autowired
	protected WebApplicationContext context;
	protected MockMvc mockMvc;
	
}

