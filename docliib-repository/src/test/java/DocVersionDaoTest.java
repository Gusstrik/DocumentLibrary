import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentVersion;
import com.strelnikov.doclib.model.documnets.Importance;
import com.strelnikov.doclib.repository.DocVersionDao;
import com.strelnikov.doclib.repository.DocumentDao;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class DocVersionDaoTest {

    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(RepositoryConfiguration.class);
    DocVersionDao docVersionDao = appContext.getBean("DocVersionJpa",DocVersionDao.class);
    private static final DocumentDao docDao = appContext.getBean("DocumentJpa",DocumentDao.class);
    private static final DatabaseCreatorJdbc creator = appContext.getBean(DatabaseCreatorJdbc.class);
    private static Document document;
    private int expected;

    @BeforeClass
    public static void beforeFileDaoTest() {
        creator.runScript("src/test/resources/insertestdb.sql");
        document= docDao.loadDocument(1);
    }

    @AfterClass
    public static void afterFileDaoTest() {
        creator.runScript("src/test/resources/deletedb.sql");
    }

    @Before
    public void beforeEachDocVersionDaoTest(){
        expected=docVersionDao.getDocVersionList(document).size();
    }

    @Test
    public void insertDocVersionTest(){
        expected+=1;
        DocumentVersion documentVersion = new DocumentVersion();
        documentVersion.setVersion(1);
        Document document = new Document();
        document.setId(1);
        documentVersion.setParentDocument(document);
        documentVersion.setDescription("another version of testDoc");
        documentVersion.setImportance(Importance.IMPORTANT);
        documentVersion.setModerated(false);
        documentVersion= docVersionDao.insertDocVersion(documentVersion);
        int actual = docVersionDao.getDocVersionList(document).size();
        docVersionDao.deleteDocVersion(documentVersion.getId());
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void deleteDocVersionTest(){
        DocumentVersion documentVersion = new DocumentVersion();
        documentVersion.setVersion(1);
        Document document = new Document();
        document.setId(1);
        documentVersion.setParentDocument(document);
        documentVersion.setDescription("another version of testDoc");
        documentVersion.setImportance(Importance.IMPORTANT);
        documentVersion.setModerated(false);
        documentVersion= docVersionDao.insertDocVersion(documentVersion);
        docVersionDao.deleteDocVersion(documentVersion.getId());
        int actual = docVersionDao.getDocVersionList(document).size();
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void loadDocVersionTest(){
        DocumentVersion docVer = docVersionDao.loadDocVersion(1);
        Assert.assertEquals("IMPORTANT",docVer.getImportance().toString());
    }

    @Test
    public void getDocVersionsListTest(){
        List<DocumentVersion> list = docVersionDao.getDocVersionList(document);
        Assert.assertEquals("test description",list.get(0).getDescription());
    }
}
