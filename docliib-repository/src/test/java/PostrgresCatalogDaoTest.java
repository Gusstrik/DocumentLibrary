import com.strelnikov.doclib.postgresdatabase.CatalogDao;
import com.strelnikov.doclib.postgresdatabase.DocumentDao;
import com.strelnikov.doclib.postgresdatabase.TypeDao;
import com.strelnikov.doclib.service.conception.Entity;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class PostrgresCatalogDaoTest {
    private static final Logger log;

    static {
        log = LoggerFactory.getLogger("PostgresTest");
    }

    private ArrayList<String> expected;
    private static int catalog_id;

    @BeforeClass
    public static void beforeCatalogTest() {
        CatalogDao catalogDao = new CatalogDao();
        catalogDao.addNewCatalog("test_1");
        TypeDao typeDao = new TypeDao();
        typeDao.addType("test_type");
        DocumentDao documentDao = new DocumentDao();
        documentDao.addNewDocuemnt("test_doc", 1, "test_type", catalogDao.getCatalogId("test_1"));
    }

    @AfterClass
    public static void afterCatalogTest() {
        CatalogDao catalogDao = new CatalogDao();
        catalogDao.deleteCatalog("test_1");
        TypeDao typeDao = new TypeDao();
        typeDao.deleteType("test_type");
    }

    @Before
    public void beforeEachTypeDaoTest() {
        CatalogDao catalogDao = new CatalogDao();
        catalogDao.addNewCatalog("test_2","test_1");
        catalog_id= catalogDao.getCatalogId("test_2");
        expected = getNamesFromContentList();
    }
    @After
    public void afterEachCatalogDaoTest(){
        CatalogDao catalogDao = new CatalogDao();
        catalogDao.deleteCatalog("test_2");
    }

    private ArrayList<String> getNamesFromContentList() {
        CatalogDao catalogDao = new CatalogDao();
        ArrayList<String> list = new ArrayList();
        for (Entity e : catalogDao.getContentList("test_1")) {
            list.add(e.getName());
        }
        return list;
    }


    @Test
    public void addCatalogTest(){
        CatalogDao catalogDao = new CatalogDao();
        catalogDao.addNewCatalog("test_3","test_1");
        expected.add(1,"test_3");
        ArrayList<String> actual = getNamesFromContentList();
        catalogDao.deleteCatalog("test_3");
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void addDeleteCatalogTest(){
        CatalogDao catalogDao = new CatalogDao();
        catalogDao.deleteCatalog("test_2");
        expected.remove("test_2");
        ArrayList<String> actual = getNamesFromContentList();
        catalogDao.addNewCatalog("test_2","test_1");
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void getCatalogIdTest(){
        CatalogDao catalogDao = new CatalogDao();
        catalogDao.addNewCatalog("test_3", "test_1");
        int actual = catalogDao.getCatalogId("test_3");
        catalogDao.deleteCatalog("test_3");
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
//        ArrayList<Entity> entities = catalogDao.getContentList("test1");
//        System.out.println(entities.get(0).getName());
//        ArrayList<String> actual = new ArrayList();
//        for (Entity e : entities) {
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
