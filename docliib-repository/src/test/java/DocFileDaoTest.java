import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.repository.DocFileDao;
import com.strelnikov.doclib.repository.DocTypeDao;
import com.strelnikov.doclib.repository.DocVersionDao;
import com.strelnikov.doclib.repository.DocumentDao;
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
    private static int document_id;

    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(RepositoryConfiguration.class);
    private static final DatabaseCreatorJdbc creator = appContext.getBean(DatabaseCreatorJdbc.class);
    private List<String> expected;
    private static final DocFileDao docFileDao = appContext.getBean("DocFileJpa", DocFileDao.class);
    private static final DocumentDao docDao = appContext.getBean("DocumentJpa",DocumentDao.class);
    private static final DocVersionDao docVerDao = appContext.getBean("DocVersionJpa", DocVersionDao.class);
    private static DocumentVersion documentVersion = new DocumentVersion();

    @BeforeClass
    public static void beforeFileDaoTest() {
        creator.runScript("src/test/resources/insertestdb.sql");
//        Document doc = docDao.loadDocument(1);
//        documentVersion=doc.getVersionsList().get(0);
    }

    @AfterClass
    public static void afterFileDaoTest() {
        creator.runScript("src/test/resources/deletedb.sql");
    }

    private List<String> convertToStringList() {
        documentVersion=docVerDao.loadDocVersion(1);
        List<String> list= new ArrayList<>();
        List<DocumentFile> fileList = docFileDao.getFilesList(documentVersion);

        for (DocumentFile df : fileList) {
            list.add(df.getFileName());
        }
        return list;
    }

    @Before
    public void beforeEachFileDaoTest() {
        expected = convertToStringList();
    }


    @Test
    public void getFileListTest() {
        List<DocumentFile> list = docFileDao.getFilesList(documentVersion);
        Assert.assertEquals("test_file", list.get(0).getFileName());
    }

    @Test
    public void insertFileTest() {
        expected.add("test_adding_file");
        DocumentFile documentFile = new DocumentFile("test_adding_file", "test_path");
        documentFile.setId(0);
        documentFile.setDocVersion(new ArrayList<>());
        documentFile.getDocVersion().add(documentVersion);
        documentFile = docFileDao.insertFile(documentFile);
        List<String> actual = convertToStringList();
        docFileDao.deleteFile(documentFile.getId());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void deleteFileTest() {
        DocumentFile documentFile = new DocumentFile("test_adding_file", "test_path");
        documentFile.setId(0);
        documentFile.setDocVersion(new ArrayList<>());
        documentFile.getDocVersion().add(documentVersion);
        documentFile = docFileDao.insertFile(documentFile);
        docFileDao.deleteFile(documentFile.getId());
        List<String> actual = convertToStringList();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void loadFileTest(){
        DocumentFile docFile = docFileDao.getFile(1);
        Assert.assertEquals("test_file",docFile.getFileName());
    }
}
