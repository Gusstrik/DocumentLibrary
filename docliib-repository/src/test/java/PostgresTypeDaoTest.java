
import com.strelnikov.doclib.postgresdatabase.*;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


public class PostgresTypeDaoTest {
    private static final Logger log;

    static {
        log = LoggerFactory.getLogger("PostgresTest");
    }

    private ArrayList<String> expected;

    @Before
    public void beforeEachTypeDaoTest() {
        TypeDao typeDao = new TypeDao();
        typeDao.addType("test_type");
        expected = typeDao.getTypesList();
    }

    @After
    public void afterEachTypeDaoTest() {
        TypeDao typeDao = new TypeDao();
        typeDao.deleteType("test_type");
    }

    @Test
    public void getTypeListTest() {
        TypeDao typeDao = new TypeDao();
        ArrayList<String> actual = typeDao.getTypesList();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addTypeTest() {
        TypeDao typeDao = new TypeDao();
        typeDao.addType("test_adding_type");
        expected.add("test_adding_type");
        ArrayList<String> actual = typeDao.getTypesList();
        typeDao.deleteType("test_adding_type");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void deleteTypeTest() {
        TypeDao typeDao = new TypeDao();
        typeDao.deleteType("test_type");
        ArrayList<String> actual = typeDao.getTypesList();
        expected.remove("test_type");
        typeDao.addType("test_type");
        Assert.assertEquals(expected, actual);
    }

}
