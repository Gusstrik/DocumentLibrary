
import com.strelnikov.doclib.repository.DocTypeDao;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

@Slf4j
public class DocDocTypeDaoJdbcTest {

    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(RepositoryConfiguration.class);
    private final DocTypeDao docTypeDao = appContext.getBean(DocTypeDao.class);
    private List<String> expected;

    @Before
    public void beforeEachTypeDaoTest() {
        docTypeDao.addType("test_type");
        expected = docTypeDao.getTypesList();
    }

    @After
    public void afterEachTypeDaoTest() {
        docTypeDao.deleteType("test_type");
    }

    @Test
    public void getTypeListTest() {
        List<String> actual = docTypeDao.getTypesList();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addTypeTest() {
        docTypeDao.addType("test_adding_type");
        expected.add("test_adding_type");
        List<String> actual = docTypeDao.getTypesList();
        docTypeDao.deleteType("test_adding_type");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void deleteTypeTest() {
        docTypeDao.deleteType("test_type");
        List<String> actual = docTypeDao.getTypesList();
        expected.remove("test_type");
        docTypeDao.addType("test_type");
        Assert.assertEquals(expected, actual);
    }

}
