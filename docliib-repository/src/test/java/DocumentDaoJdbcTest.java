import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.model.documnets.DocumentType;
import com.strelnikov.doclib.repository.DocumentDao;
import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

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

    @Test
    public void loadDocuemntTest(){
        Unit unit = new Document();
        unit.setId(1);
        Document document = documentDao.loadDocument(unit.getId());
        Assert.assertEquals("test_doc",document.getName());
    }

    @Test
    public void getDocumentsListTest(){
        List<Unit> list = documentDao.getDocumentsList(1);
        Assert.assertEquals("test_doc", list.get(0).getName());
    }

    @Test
    public void updateDocuemntTest(){
        Unit unit = new Document();
        unit.setId(1);
        Document document = documentDao.loadDocument(unit.getId());
        document.setName("changed name");
        documentDao.updateDocument(document);
        Document actual = documentDao.loadDocument(unit.getId());
        document.setName("test_doc");
        documentDao.updateDocument(document);
        Assert.assertEquals("changed name",actual.getName());
    }

    @Test
    public void insertDocumentTest(){
        Document document = new Document();
        document.setName("test doc 2");
        document.setParent_id(1);
        document.setDocumentType(new DocumentType("test_type"));
        document = documentDao.insertDocument(document);
        int actual = documentDao.getDocumentsList(1).size();
        documentDao.deleteDocument(document.getId());
        Assert.assertEquals(2,actual);
    }

    @Test
    public void deleteDocumentTest(){
        Document document = new Document();
        document.setName("test doc 2");
        document.setParent_id(1);
        document.setDocumentType(new DocumentType("test_type"));
        document = documentDao.insertDocument(document);
        documentDao.deleteDocument(document.getId());
        int actual = documentDao.getDocumentsList(1).size();
        Assert.assertEquals(1,actual);
    }

}
