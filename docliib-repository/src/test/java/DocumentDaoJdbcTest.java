import com.strelnikov.doclib.repository.DocumentDao;
import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.repository.jdbc.DatabaseConnectorJdbc;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentVersion;
import com.strelnikov.doclib.repository.jdbc.configuration.RepositoryConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

@Slf4j
public class DocumentDaoJdbcTest {

    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(RepositoryConfiguration.class);

    private final DocumentDao documentDao = appContext.getBean(DocumentDao.class);
    private static final DatabaseCreatorJdbc creator = appContext.getBean(DatabaseCreatorJdbc.class);

    @BeforeClass
    public static void beforeFileDaoTest() {
        creator.runScript("src/test/resources/insertestdb.sql");
    }

    @AfterClass
    public static void afterFileDaoTest() {
        creator.runScript("src/test/resources/deletedb.sql");
    }

    private int document_id;

    private ArrayList<Integer> getVersionList(){
        ArrayList<Integer> list;
        list = new ArrayList<>();
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
        Document testDoc = new Document("test_doc");
        testDoc.getDocumentType().setCurentType("test_type");
        testDoc.setActualVersion(2);
        testDoc.getVersionsList().add(new DocumentVersion("test description", false));
        testDoc.getVersionsList().add(new DocumentVersion("test description", false));
        documentDao.addNewDocument(testDoc, 1);
        document_id = documentDao.getDocumentId(testDoc);
        expected=getVersionList();
    }

    @After
    public void afterEachDocumentTest(){
        documentDao.deleteDocument(document_id);
    }

    @Test
    public void addDocumentTest(){
        Document testDoc = new Document("test_doc");
        testDoc.getDocumentType().setCurentType("test_type");
        testDoc.setActualVersion(3);
        testDoc.getVersionsList().add(new DocumentVersion("test description", false));
        testDoc.getVersionsList().add(new DocumentVersion("test description", false));
        testDoc.getVersionsList().add(new DocumentVersion("test description", false));
        documentDao.addNewDocument(testDoc, 1);
        ArrayList<Integer>actual=getVersionList();
        expected.add(3);
        documentDao.deleteDocument(documentDao.getDocumentId(testDoc));
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void deleteDocumentTest(){
        documentDao.deleteDocument(document_id);
        ArrayList<Integer> actual = getVersionList();
        expected.remove(1);
        Document testDoc = new Document("test_doc");
        testDoc.getDocumentType().setCurentType("test_type");
        testDoc.setActualVersion(2);
        testDoc.getVersionsList().add(new DocumentVersion("test description", false));
        testDoc.getVersionsList().add(new DocumentVersion("test description", false));
        documentDao.addNewDocument(testDoc, 1);
        document_id = documentDao.getDocumentId(testDoc);
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void getDocumentIdTest(){
        Document testDoc = new Document("test_doc");
        testDoc.getDocumentType().setCurentType("test_type");
        testDoc.setActualVersion(3);
        testDoc.getVersionsList().add(new DocumentVersion("test description", false));
        testDoc.getVersionsList().add(new DocumentVersion("test description", false));
        testDoc.getVersionsList().add(new DocumentVersion("test description", false));
        documentDao.addNewDocument(testDoc, 1);
        int actual = documentDao.getDocumentId(testDoc);
        documentDao.deleteDocument(actual);
        Assert.assertEquals(document_id+1,actual);
    }

    @Test
    public void loadDocumentTest(){
        Document document = documentDao.loadDocument("test_doc","test_type");
        DocumentVersion docVersion = document.getVersionsList().get(document.getActualVersion()-1);
        Assert.assertEquals("test description", docVersion.getDescription());
    }
}
