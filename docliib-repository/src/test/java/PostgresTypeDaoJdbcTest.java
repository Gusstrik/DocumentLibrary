
import com.strelnikov.doclib.database.jdbc.TypeDaoJdbc;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PostgresTypeDaoJdbcTest {


    private List<String> expected;

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
        List<String> actual = typeDaoJdbc.getTypesList();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addTypeTest() {
        TypeDaoJdbc typeDaoJdbc = new TypeDaoJdbc();
        typeDaoJdbc.addType("test_adding_type");
        expected.add("test_adding_type");
        List<String> actual = typeDaoJdbc.getTypesList();
        typeDaoJdbc.deleteType("test_adding_type");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void deleteTypeTest() {
        TypeDaoJdbc typeDaoJdbc = new TypeDaoJdbc();
        typeDaoJdbc.deleteType("test_type");
        List<String> actual = typeDaoJdbc.getTypesList();
        expected.remove("test_type");
        typeDaoJdbc.addType("test_type");
        Assert.assertEquals(expected, actual);
    }

}
