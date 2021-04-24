
import com.strelnikov.doclib.postgresdatabase.*;
import org.checkerframework.checker.units.qual.A;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;

import com.strelnikov.doclib.service.conception.Entity;


public class PostgresTest {
    private static final Logger log;

    static {
        log = LoggerFactory.getLogger("PostgresTest");
    }

    @Test
    public void connectAndCreationTest() {
        DatabaseCreator creator = new DatabaseCreator();
        creator.createDatabse();
        String homeCatName = "";
        log.info("Database was successfully created");
        try {
            Connection connection = DatabaseConnector.getConnectionFromPool();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select name from catalog where name='/'");
            rs.next();
            homeCatName = rs.getString(1);
        } catch (SQLException e) {
            log.error(e.getMessage(), e.getSQLState());
        }
        Assert.assertEquals("/", homeCatName);
    }

//    @Before
//    public void beforeTypeDaoTest(){
//        DatabaseCreator creator = new DatabaseCreator();
//        creator.createDatabse();
//    }
//    @Test
//    public void typeDaoTest() {
//        TypeDao typeDao = new TypeDao();
//        typeDao.addType("order");
//        typeDao.addType("mail");
//        typeDao.addType("fax");
//        typeDao.deleteType("order");
//        ArrayList<String> expected = new ArrayList();
//        expected.add("mail");
//        expected.add("fax");
//        ArrayList<String> actual = typeDao.getTypesList();
//        typeDao.addType("test_type");
//        typeDao.deleteType("mail");
//        typeDao.deleteType("fax");
//        Assert.assertEquals(expected, actual);
//    }

    @Before
    public void beforeFileDaoTest(){
        DocumentDao documentDao = new DocumentDao();
        documentDao.addNewDocuemnt("test_doc","test_type",1);
    }

    @Test
    public void fileDaoTest() {
        DocumentDao documentDao=new DocumentDao();
        int docId= documentDao.getDocumentId("test_doc","test_type",1);
        FileDao fileDao = new FileDao();
        fileDao.addNewFile("simple", docId, "x");
        fileDao.addNewFile("tests", docId, "y");
        fileDao.addNewFile("files", docId, "z");
        fileDao.deleteFile("tests");
        fileDao.renameFile("SIMPLEST", "simple");
        ArrayList<String> expected = new ArrayList();
        expected.add("files");
        expected.add("SIMPLEST");
        ArrayList<String> actual = fileDao.getFilesList(1);
        fileDao.deleteFile("SIMPLEST");
        fileDao.deleteFile("tests");
        Assert.assertEquals(expected, actual);
    }

    @After
    public void afterFileDaoTest(){
        DocumentDao documentDao = new DocumentDao();
        documentDao.deleteDocument(documentDao.getDocumentId("test_doc","test_type",1));
    }

    @Before
    public void beforeDocumentDaoTest(){
        TypeDao typeDao = new TypeDao();
        typeDao.addType("test_type");
    }
    @Test
    public void DocumentDaoTest() {
        DocumentDao documentDao = new DocumentDao();
        documentDao.addNewDocument("test_doc1", "test_type", 1, "test desciption",1);
        documentDao.addNewDocuemnt("test_doc1", "test_type", 2);
        documentDao.addNewDocuemnt("test_doc1", 3, "test_type", 1);
        documentDao.deleteDocument(documentDao.getDocumentId("test_doc1","test_type",2));
        ArrayList<Integer> actual = new ArrayList();
        ArrayList<Integer> expected = new ArrayList();
        expected.add(1);
        expected.add(3);
        try {
            Connection connection = DatabaseConnector.getConnectionFromPool();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT version from document where name='test_doc1';");
            while (rs.next()) {
                actual.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        Assert.assertEquals(expected,actual);
    }
    @After
    public void afterDocumentDaoTest(){
        TypeDao typeDao = new TypeDao();
        typeDao.deleteType("test_type");
    }
    @Before
    public void beforeCatalogDaoTest(){
        TypeDao typeDao = new TypeDao();
        typeDao.addType("test_type");
    }
    @Test
    public void CatalogDaoTest(){
        CatalogDao catalogDao = new CatalogDao();
        DocumentDao documentDao = new DocumentDao();
        catalogDao.addNewCatalog("test1","/");
        catalogDao.addNewCatalog("test2","test1");
        catalogDao.addNewCatalog("test3","test1");
        catalogDao.addNewCatalog("test4","test1");
        catalogDao.deleteCatalog("test3");
        documentDao.addNewDocuemnt("testing_doc",1,"test_type",catalogDao.getCatalogId("test1"));
        ArrayList<Entity> entities = catalogDao.getContentList("test1");
        System.out.println(entities.get(0).getName());
        ArrayList<String> actual = new ArrayList();
        for(Entity e:entities){
            actual.add(e.getName());
        }
        ArrayList<String> expected = new ArrayList();
        expected.add("test2");
        expected.add("test4");
        expected.add("testing_doc");
        System.out.println(actual);
        documentDao.deleteDocument(documentDao.getDocumentId("testing_doc","test_type",1));
        catalogDao.deleteCatalog("test1");
        Assert.assertEquals(expected,actual);
    }

    @After
    public void afterCatalogDaoTest(){
        TypeDao typeDao = new TypeDao();
        typeDao.deleteType("test_type");
    }

}
