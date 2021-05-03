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

    private static Catalog parentCat;

    @BeforeClass
    public static void beforeFileDaoTest() {
        parentCat=new Catalog();
        parentCat.setId(1);
        parentCat.setName("/");
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
        parentCat=catalogDao.loadCatalog(parentCat.getId());
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
        catalog.setParent_id(1);
        catalog.setName("test_1");
        catalog = catalogDao.insertCatalog(catalog);
        int amount = expected.size()+1;
        int actual = getNamesFromContentList().size();
        catalogDao.deleteCatalog(catalog.getId());
        Assert.assertEquals(amount, actual);
    }

    @Test
    public void deleteCatalogTest() {
        Catalog catalog = new Catalog();
        catalog.setParent_id(1);
        catalog.setName("test_1");
        catalog = catalogDao.insertCatalog(catalog);
        catalogDao.deleteCatalog(catalog.getId());
        ArrayList<String> actual = getNamesFromContentList();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void loadCatalogTest() {
        Catalog test = catalogDao.loadCatalog(1);
        Assert.assertEquals("/", test.getName());
    }

    @Test
    public void updateCatalogTest(){
        Catalog catalog = new Catalog();
        catalog.setName("test catalog");
        catalog.setParent_id(1);
        catalog=catalogDao.insertCatalog(catalog);
        catalog.setName("changed_name");
        catalogDao.updateCatalog(catalog);
        Catalog test = catalogDao.loadCatalog(catalog.getId());
        catalogDao.deleteCatalog(catalog.getId());
        Assert.assertEquals("changed_name",test.getName());
    }
}
