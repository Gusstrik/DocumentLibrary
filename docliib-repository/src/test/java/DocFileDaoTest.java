import com.strelnikov.doclib.repository.DocFileDao;
import com.strelnikov.doclib.repository.DocVersionDao;
import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.model.documnets.DocumentFile;
import com.strelnikov.doclib.model.documnets.DocumentVersion;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DocFileDaoTest {

    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(RepositoryConfiguration.class);
    private static final DatabaseCreatorJdbc creator = appContext.getBean(DatabaseCreatorJdbc.class);
    private static final DocFileDao docFileDao = appContext.getBean("DocFileJpa", DocFileDao.class);
    private static final DocVersionDao docVerDao = appContext.getBean("DocVersionJpa", DocVersionDao.class);

    @BeforeClass
    public static void beforeFileDaoTest() {
        creator.runScript("src/test/resources/insertestdb.sql");
    }

    @AfterClass
    public static void afterFileDaoTest() {
        creator.runScript("src/test/resources/deletedb.sql");
    }

    @Test
    public void getFileListTest() {
        DocumentVersion documentVersion =docVerDao.loadDocVersion(1);
        List<DocumentFile> list = docFileDao.getFilesList(documentVersion);
        Assert.assertEquals("test_file", list.get(0).getFileName());
    }

    @Test
    public void insertFileTest() {
        DocumentFile documentFile = new DocumentFile("test_adding_file", "test_path");
        documentFile.setId(0);
        documentFile.setDocVersion(new ArrayList<>());
        documentFile = docFileDao.insertFile(documentFile);
        String actual = docFileDao.getFile(documentFile.getId()).getFileName();
        docFileDao.deleteFile(documentFile.getId());
        Assert.assertEquals("test_adding_file", actual);
    }

    @Test
    public void deleteFileTest() {
        DocumentFile documentFile = new DocumentFile("test_adding_file", "test_path");
        documentFile.setId(0);
        documentFile.setDocVersion(new ArrayList<>());
        documentFile = docFileDao.insertFile(documentFile);
        docFileDao.deleteFile(documentFile.getId());
        Assert.assertNull(docFileDao.getFile(documentFile.getId()));
    }

    @Test
    public void loadFileTest(){
        DocumentFile docFile = docFileDao.getFile(1);
        Assert.assertEquals("test_file",docFile.getFileName());
    }

    @Test
    public void loadFileByNameTest(){
        DocumentFile docFile = docFileDao.getFile("test_file");
        Assert.assertEquals(1,docFile.getId());
    }
}
