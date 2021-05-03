import com.strelnikov.doclib.model.documnets.DocumentVersion;
import com.strelnikov.doclib.model.documnets.Importance;
import com.strelnikov.doclib.repository.DocVersionDao;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DocVersionDaoTest {

    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(RepositoryConfiguration.class);
    DocVersionDao docVersionDao = appContext.getBean(DocVersionDao.class);
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
    public void getDocVersionIdTest(){
        DocumentVersion documentVersion = new DocumentVersion();
        documentVersion.setVersion(0);
        int actual = docVersionDao.getDocVersionId(documentVersion,1);
        Assert.assertEquals(1,actual);
    }

    @Test
    public void loadDocVersionTest(){
        DocumentVersion documentVersion = docVersionDao.loadDocVersion(1,0);
        Assert.assertEquals("test description",documentVersion.getDescription());
    }

    @Test
    public void addDocVersionTest(){
        DocumentVersion documentVersion = new DocumentVersion();
        documentVersion.setVersion(1);
        documentVersion.setDescription("another version of testDoc");
        documentVersion.setImportance(Importance.IMPORTANT);
        documentVersion.setModerated(false);
        docVersionDao.addNewDocVersion(documentVersion, 1);
        DocumentVersion actual = docVersionDao.loadDocVersion(1,documentVersion.getVersion());
        docVersionDao.deleteDocVersion(docVersionDao.getDocVersionId(documentVersion,1));
        Assert.assertEquals(documentVersion.getDescription(),actual.getDescription());
    }

    @Test
    public void deleteDocVersionTest(){
        DocumentVersion documentVersion = new DocumentVersion();
        documentVersion.setVersion(1);
        documentVersion.setDescription("another version of testDoc");
        documentVersion.setImportance(Importance.IMPORTANT);
        documentVersion.setModerated(false);
        docVersionDao.deleteDocVersion(docVersionDao.getDocVersionId(documentVersion,1));
        DocumentVersion actual = docVersionDao.loadDocVersion(1,documentVersion.getVersion());
        Assert.assertNull(actual);
    }
}
