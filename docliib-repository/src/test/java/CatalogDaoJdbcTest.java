import com.strelnikov.doclib.repository.CatalogDao;
import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;

import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CatalogDaoJdbcTest {

    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(RepositoryConfiguration.class);

    private ArrayList<String> expected;
    private final CatalogDao catalogDao = appContext.getBean(CatalogDao.class);
    private static final DatabaseCreatorJdbc databaseCreatorJdbc=appContext.getBean(DatabaseCreatorJdbc.class);

    private Catalog parentCat;

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
        parentCat = catalogDao.loadCatalog("test_parent");
        ArrayList<String> list;
        list = new ArrayList<>();
        for (Unit e : parentCat.getContentList()) {
            list.add(e.getName());
        }
        return list;
    }


    @Test
    public void addCatalogTest() {
        Catalog catalog = new Catalog();
        catalog.setParent("test_parent");
        catalog.setName("test_3");
        catalogDao.addNewCatalog(catalog);
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
        Catalog catalog = new Catalog();
        catalog.setName("test_2");
        catalog.setParent("test_parent");
        catalogDao.addNewCatalog(catalog);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getCatalogIdTest() {
        Catalog catalog = new Catalog();
        catalog.setParent("test_parent");
        catalog.setName("test_3");
        catalogDao.addNewCatalog(catalog);
        int expected = catalogDao.getCatalogId("test_3") + 2;
        catalogDao.deleteCatalog("test_3");
        catalogDao.addNewCatalog(catalog);
        catalogDao.deleteCatalog("test_3");
        catalogDao.addNewCatalog(catalog);
        int actual = catalogDao.getCatalogId("test_3");
        catalogDao.deleteCatalog("test_3");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void loadCatalogTest() {
        Catalog test = catalogDao.loadCatalog("test_parent");
        List<String> actual;
        actual = new ArrayList<>();
        for (Unit u : test.getContentList()) {
            actual.add(u.getName());
        }
        Assert.assertEquals(expected, actual);
    }
}
