package test.repository;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/resources/spring/beans-dao.xml")
public abstract class BaseRepositoryTest {

	@Autowired
	protected ApplicationContext context;
	@PersistenceContext
	protected EntityManager entityManager;

	public void deleteAllObjects() {
		entityManager.createQuery("delete from Customer").executeUpdate();
		entityManager.createQuery("delete from Product").executeUpdate();
		entityManager.createQuery("delete from Category").executeUpdate();
		entityManager.createQuery("delete from User").executeUpdate();
	}

	public byte[] getImage() {
		File file = null;
		InputStream is = null;
		byte[] image = null;

		try {
			file = new File(context.getResource("classpath:image/image.jpg").getURI());
			is = new BufferedInputStream(new FileInputStream(file));
			image = new byte[(int) file.length()];
			is.read(image);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return image;
	}

}
