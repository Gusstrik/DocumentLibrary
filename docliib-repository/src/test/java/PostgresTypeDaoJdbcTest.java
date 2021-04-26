
import com.strelnikov.doclib.database.jdbc.TypeDaoJdbc;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


public class PostgresTypeDaoJdbcTest {
    private static final Logger log;

    static {
        log = LoggerFactory.getLogger("PostgresTest");
    }

    private ArrayList<String> expected;

    @Before
    public void beforeEachTypeDaoTest() {
        TypeDaoJdbc typeDaoJdbc = new TypeDaoJdbc();
        typeDaoJdbc.addType("test_type");
        expected = typeDaoJdbc.getTypesList();
    }

    @After
    public void afterEachTypeDaoTest() {
        TypeDaoJdbc typeDaoJdbc = new TypeDaoJdbc();
        typeDaoJdbc.deleteType("test_type");
    }

    @Test
    public void getTypeListTest() {
        TypeDaoJdbc typeDaoJdbc = new TypeDaoJdbc();
        ArrayList<String> actual = typeDaoJdbc.getTypesList();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addTypeTest() {
        TypeDaoJdbc typeDaoJdbc = new TypeDaoJdbc();
        typeDaoJdbc.addType("test_adding_type");
        expected.add("test_adding_type");
        ArrayList<String> actual = typeDaoJdbc.getTypesList();
        typeDaoJdbc.deleteType("test_adding_type");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void deleteTypeTest() {
        TypeDaoJdbc typeDaoJdbc = new TypeDaoJdbc();
        typeDaoJdbc.deleteType("test_type");
        ArrayList<String> actual = typeDaoJdbc.getTypesList();
        expected.remove("test_type");
        typeDaoJdbc.addType("test_type");
        Assert.assertEquals(expected, actual);
    }

}
