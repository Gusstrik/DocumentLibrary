import com.strelnikov.doclib.repository.CatalogDao;
import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;

import com.strelnikov.doclib.repository.jdbc.CatalogDaoJdbc;
import com.strelnikov.doclib.repository.jdbc.DocumentDaoJdbc;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.repository.jdbc.configuration.RepositoryConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CatalogDaoJdbcTest {

    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(RepositoryConfiguration.class);

    private ArrayList<String> expected;
    private CatalogDao catalogDao = appContext.getBean(CatalogDaoJdbc.class);
    private static DatabaseCreatorJdbc databaseCreatorJdbc=appContext.getBean(DatabaseCreatorJdbc.class);

    @BeforeClass
    public static void beforeFileDaoTest() {
        databaseCreatorJdbc.runScript("src/test/resources/insertestdb.sql");
    }

    @AfterClass
    public static void afterFileDaoTest() {
        databaseCreatorJdbc.runScript("src/test/resources/deletedb.sql");
    }

    @Before
    public void beforeEachTypeDaoTest() {
        expected = getNamesFromContentList();
    }


    private ArrayList<String> getNamesFromContentList() {
        ArrayList<String> list = new ArrayList();
        for (Unit e : catalogDao.getContentList("test_parent")) {
            list.add(e.getName());
        }
        return list;
    }


    @Test
    public void addCatalogTest() {
        catalogDao.addNewCatalog("test_3", "test_parent");
        expected.add(2, "test_3");
        ArrayList<String> actual = getNamesFromContentList();
        catalogDao.deleteCatalog("test_3");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void deleteCatalogTest() {
        catalogDao.deleteCatalog("test_2");
        expected.remove("test_2");
        ArrayList<String> actual = getNamesFromContentList();
        catalogDao.addNewCatalog("test_2", "test_parent");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getCatalogIdTest() {
        catalogDao.addNewCatalog("test_3", "test_parent");
        int expected = catalogDao.getCatalogId("test_3") + 2;
        catalogDao.deleteCatalog("test_3");
        catalogDao.addNewCatalog("test_3", "test_parent");
        catalogDao.deleteCatalog("test_3");
        catalogDao.addNewCatalog("test_3", "test_parent");
        int actual = catalogDao.getCatalogId("test_3");
        catalogDao.deleteCatalog("test_3");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void loadCatalogTest() {
        Catalog test = catalogDao.loadCatalog("test_parent");
        test.setContentList(catalogDao.getContentList(test.getName()));
        List<String> actual = new ArrayList();
        for (Unit u : test.getContentList()) {
            actual.add(u.getName());
        }
        Assert.assertEquals(expected, actual);
    }
}
