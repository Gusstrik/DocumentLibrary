import com.strelnikov.doclib.database.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.database.jdbc.TypeDaoJdbc;
import com.strelnikov.doclib.database.jdbc.CatalogDaoJdbc;
import com.strelnikov.doclib.database.jdbc.DocumentDaoJdbc;
import com.strelnikov.doclib.model.conception.Unit;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

@Slf4j
public class PostrgresCatalogDaoJdbcTest {

    private ArrayList<String> expected;
    private static int catalog_id;

    @BeforeClass
    public static void beforeFileDaoTest() {
        DatabaseCreatorJdbc creator = new DatabaseCreatorJdbc();
        creator.runScript("src/test/resources/insertestdb.sql");
        DocumentDaoJdbc documentDaoJdbc = new DocumentDaoJdbc();
    }

    @AfterClass
    public static void afterFileDaoTest() {
        DatabaseCreatorJdbc creator = new DatabaseCreatorJdbc();
        creator.runScript("src/test/resources/deletedb.sql");
    }

    @Before
    public void beforeEachTypeDaoTest() {
        CatalogDaoJdbc catalogDaoJdbc = new CatalogDaoJdbc();
        catalogDaoJdbc.addNewCatalog("test_2", "test_1");
        catalog_id = catalogDaoJdbc.getCatalogId("test_2");
        expected = getNamesFromContentList();
    }

    @After
    public void afterEachCatalogDaoTest() {
        CatalogDaoJdbc catalogDaoJdbc = new CatalogDaoJdbc();
        catalogDaoJdbc.deleteCatalog("test_2");
    }

    private ArrayList<String> getNamesFromContentList() {
        CatalogDaoJdbc catalogDaoJdbc = new CatalogDaoJdbc();
        ArrayList<String> list = new ArrayList();
        for (Unit e : catalogDaoJdbc.getContentList("test_1")) {
            list.add(e.getName());
        }
        return list;
    }


    @Test
    public void addCatalogTest() {
        CatalogDaoJdbc catalogDaoJdbc = new CatalogDaoJdbc();
        catalogDaoJdbc.addNewCatalog("test_3", "test_1");
        expected.add(1, "test_3");
        ArrayList<String> actual = getNamesFromContentList();
        catalogDaoJdbc.deleteCatalog("test_3");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addDeleteCatalogTest() {
        CatalogDaoJdbc catalogDaoJdbc = new CatalogDaoJdbc();
        catalogDaoJdbc.deleteCatalog("test_2");
        expected.remove("test_2");
        ArrayList<String> actual = getNamesFromContentList();
        catalogDaoJdbc.addNewCatalog("test_2", "test_1");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getCatalogIdTest() {
        CatalogDaoJdbc catalogDaoJdbc = new CatalogDaoJdbc();
        catalogDaoJdbc.addNewCatalog("test_3", "test_1");
        int actual = catalogDaoJdbc.getCatalogId("test_3");
        catalogDaoJdbc.deleteCatalog("test_3");
        Assert.assertEquals(catalog_id + 1, actual);
    }
}
