import com.strelnikov.doclib.database.jdbc.TypeDaoJdbc;
import com.strelnikov.doclib.database.jdbc.CatalogDaoJdbc;
import com.strelnikov.doclib.database.jdbc.DocumentDaoJdbc;
import com.strelnikov.doclib.model.conception.Unit;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class PostrgresCatalogDaoJdbcTest {
    private static final Logger log;

    static {
        log = LoggerFactory.getLogger("PostgresTest");
    }

    private ArrayList<String> expected;
    private static int catalog_id;

    @BeforeClass
    public static void beforeCatalogTest() {
        CatalogDaoJdbc catalogDaoJdbc = new CatalogDaoJdbc();
        catalogDaoJdbc.addNewCatalog("test_1");
        TypeDaoJdbc typeDaoJdbc = new TypeDaoJdbc();
        typeDaoJdbc.addType("test_type");
        DocumentDaoJdbc documentDaoJdbc = new DocumentDaoJdbc();
        documentDaoJdbc.addNewDocuemnt("test_doc", 1, "test_type", catalogDaoJdbc.getCatalogId("test_1"));
    }

    @AfterClass
    public static void afterCatalogTest() {
        CatalogDaoJdbc catalogDaoJdbc = new CatalogDaoJdbc();
        catalogDaoJdbc.deleteCatalog("test_1");
        TypeDaoJdbc typeDaoJdbc = new TypeDaoJdbc();
        typeDaoJdbc.deleteType("test_type");
    }

    @Before
    public void beforeEachTypeDaoTest() {
        CatalogDaoJdbc catalogDaoJdbc = new CatalogDaoJdbc();
        catalogDaoJdbc.addNewCatalog("test_2","test_1");
        catalog_id= catalogDaoJdbc.getCatalogId("test_2");
        expected = getNamesFromContentList();
    }
    @After
    public void afterEachCatalogDaoTest(){
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
    public void addCatalogTest(){
        CatalogDaoJdbc catalogDaoJdbc = new CatalogDaoJdbc();
        catalogDaoJdbc.addNewCatalog("test_3","test_1");
        expected.add(1,"test_3");
        ArrayList<String> actual = getNamesFromContentList();
        catalogDaoJdbc.deleteCatalog("test_3");
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void addDeleteCatalogTest(){
        CatalogDaoJdbc catalogDaoJdbc = new CatalogDaoJdbc();
        catalogDaoJdbc.deleteCatalog("test_2");
        expected.remove("test_2");
        ArrayList<String> actual = getNamesFromContentList();
        catalogDaoJdbc.addNewCatalog("test_2","test_1");
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void getCatalogIdTest(){
        CatalogDaoJdbc catalogDaoJdbc = new CatalogDaoJdbc();
        catalogDaoJdbc.addNewCatalog("test_3", "test_1");
        int actual = catalogDaoJdbc.getCatalogId("test_3");
        catalogDaoJdbc.deleteCatalog("test_3");
        Assert.assertEquals(catalog_id+1,actual);
    }


    //    @Test
//    public void CatalogDaoTest() {
//        CatalogDao catalogDao = new CatalogDao();
//        DocumentDao documentDao = new DocumentDao();
//        catalogDao.addNewCatalog("test1", "/");
//        catalogDao.addNewCatalog("test2", "test1");
//        catalogDao.addNewCatalog("test3", "test1");
//        catalogDao.addNewCatalog("test4", "test1");
//        catalogDao.deleteCatalog("test3");
//        documentDao.addNewDocuemnt("testing_doc", 1, "test_type", catalogDao.getCatalogId("test1"));
//        ArrayList<Unit> entities = catalogDao.getContentList("test1");
//        System.out.println(entities.get(0).getName());
//        ArrayList<String> actual = new ArrayList();
//        for (Unit e : entities) {
//            actual.add(e.getName());
//        }
//        ArrayList<String> expected = new ArrayList();
//        expected.add("test2");
//        expected.add("test4");
//        expected.add("testing_doc");
//        System.out.println(actual);
//        documentDao.deleteDocument(documentDao.getDocumentId("testing_doc", "test_type", 1));
//        catalogDao.deleteCatalog("test1");
//        Assert.assertEquals(expected, actual);
//    }
}
