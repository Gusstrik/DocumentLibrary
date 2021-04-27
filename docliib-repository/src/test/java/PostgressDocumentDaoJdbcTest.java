import com.strelnikov.doclib.database.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.database.jdbc.TypeDaoJdbc;
import com.strelnikov.doclib.database.jdbc.DatabaseConnectorJdbc;
import com.strelnikov.doclib.database.jdbc.DocumentDaoJdbc;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

@Slf4j
public class PostgressDocumentDaoJdbcTest {

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

    private int document_id;

    private ArrayList<Integer> getVersionList(){
        ArrayList<Integer> list = new ArrayList();
        try ( Connection connection = DatabaseConnectorJdbc.getConnectionFromPool()) {
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
        DocumentDaoJdbc documentDaoJdbc = new DocumentDaoJdbc();
        documentDaoJdbc.addNewDocument("test_doc", "test_type", 2, "test document", 1);
        document_id = documentDaoJdbc.getDocumentId("test_doc", "test_type", 2);
        expected=getVersionList();
    }

    @After
    public void afterEachDocumentTest(){
        DocumentDaoJdbc documentDaoJdbc = new DocumentDaoJdbc();
        documentDaoJdbc.deleteDocument(document_id);
    }

    @Test
    public void addDocumentTest(){
        DocumentDaoJdbc documentDaoJdbc = new DocumentDaoJdbc();
        documentDaoJdbc.addNewDocument("test_doc", "test_type", 3, "test document", 1);
        ArrayList<Integer>actual=getVersionList();
        expected.add(3);
        documentDaoJdbc.deleteDocument(documentDaoJdbc.getDocumentId("test_doc","test_type",3));
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void deleteDocumentTest(){
        DocumentDaoJdbc documentDaoJdbc = new DocumentDaoJdbc();
        documentDaoJdbc.deleteDocument(document_id);
        ArrayList<Integer> actual = getVersionList();
        expected.remove(1);
        documentDaoJdbc.addNewDocument("test_doc", "test_type", 2, "test document", 1);
        document_id = documentDaoJdbc.getDocumentId("test_doc", "test_type", 2);
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void getDocumentIdTest(){
        DocumentDaoJdbc documentDaoJdbc = new DocumentDaoJdbc();
        documentDaoJdbc.addNewDocument("test_doc", "test_type", 3, "test document", 1);
        int actual = documentDaoJdbc.getDocumentId("test_doc","test_type",3);
        documentDaoJdbc.deleteDocument(actual);
        Assert.assertEquals(document_id+1,actual);
    }
}
