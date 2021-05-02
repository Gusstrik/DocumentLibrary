
import com.strelnikov.doclib.repository.TypeDao;
import com.strelnikov.doclib.repository.jdbc.TypeDaoJdbc;
import com.strelnikov.doclib.repository.jdbc.configuration.RepositoryConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

@Slf4j
public class UnitTypeDaoJdbcTest {

    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(RepositoryConfiguration.class);
    private final TypeDao typeDao = appContext.getBean(TypeDao.class);
    private List<String> expected;

    @Before
    public void beforeEachTypeDaoTest() {
        typeDao.addType("test_type");
        expected = typeDao.getTypesList();
    }

    @After
    public void afterEachTypeDaoTest() {
        typeDao.deleteType("test_type");
    }

    @Test
    public void getTypeListTest() {
        List<String> actual = typeDao.getTypesList();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addTypeTest() {
        typeDao.addType("test_adding_type");
        expected.add("test_adding_type");
        List<String> actual = typeDao.getTypesList();
        typeDao.deleteType("test_adding_type");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void deleteTypeTest() {
        typeDao.deleteType("test_type");
        List<String> actual = typeDao.getTypesList();
        expected.remove("test_type");
        typeDao.addType("test_type");
        Assert.assertEquals(expected, actual);
    }

}
