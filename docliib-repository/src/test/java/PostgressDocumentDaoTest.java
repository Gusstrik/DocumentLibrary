import com.strelnikov.doclib.postgresdatabase.DatabaseConnector;
import com.strelnikov.doclib.postgresdatabase.DocumentDao;
import com.strelnikov.doclib.postgresdatabase.TypeDao;
import org.checkerframework.checker.units.qual.A;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PostgressDocumentDaoTest {
    private static final Logger log;

    static {
        log = LoggerFactory.getLogger("PostgresTest");
    }



    @BeforeClass
    public static void beforeDocumentTests() {
        TypeDao typeDao = new TypeDao();
        typeDao.addType("test_type");
    }

    @AfterClass
    public static void afterDocumentTests() {
        TypeDao typeDao = new TypeDao();
        typeDao.deleteType("test_type");
    }

    private int document_id;

    private ArrayList<Integer> getVersionList(){
        ArrayList<Integer> list = new ArrayList();
        try {
            Connection connection = DatabaseConnector.getConnectionFromPool();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT version from document where name='test_doc';");
            while (rs.next()) {
                list.add(rs.getInt(1));
            }
            connection.close();
            System.out.println(list);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }

    private ArrayList<Integer> expected;

    @Before
    public void beforeEachDocumentTest() {
        DocumentDao documentDao = new DocumentDao();
        documentDao.addNewDocument("test_doc", "test_type", 1, "test document", 1);
        document_id =documentDao.getDocumentId("test_doc", "test_type", 1);
        expected=getVersionList();
    }

    @After
    public void afterEachDocumentTest(){
        DocumentDao documentDao = new DocumentDao();
        documentDao.deleteDocument(document_id);
    }

    @Test
    public void addDocumentTest(){
        DocumentDao documentDao = new DocumentDao();
        documentDao.addNewDocument("test_doc", "test_type", 2, "test document", 1);
        ArrayList<Integer>actual=getVersionList();
        expected.add(2);
        documentDao.deleteDocument(documentDao.getDocumentId("test_doc","test_type",2));
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void deleteDocumentTest(){
        DocumentDao documentDao = new DocumentDao();
        documentDao.deleteDocument(document_id);
        ArrayList<Integer> actual = getVersionList();
        expected.remove(0);
        documentDao.addNewDocument("test_doc", "test_type", 1, "test document", 1);
        document_id =documentDao.getDocumentId("test_doc", "test_type", 1);
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void getDocumentIdTest(){
        DocumentDao documentDao = new DocumentDao();
        documentDao.addNewDocument("test_doc", "test_type", 2, "test document", 1);
        int actual = documentDao.getDocumentId("test_doc","test_type",2);
        documentDao.deleteDocument(actual);
        Assert.assertEquals(document_id+1,actual);
    }
}
