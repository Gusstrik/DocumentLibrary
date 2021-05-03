import com.strelnikov.doclib.repository.DocumentDao;
import com.strelnikov.doclib.repository.FileDao;
import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.model.documnets.Document;
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
public class DocumentFileDaoJdbcTest {
    private static int document_id;

    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(RepositoryConfiguration.class);
    private static final DatabaseCreatorJdbc creator = appContext.getBean(DatabaseCreatorJdbc.class);
    private static final DocumentDao documentDao = appContext.getBean(DocumentDao.class);
    private List<String> expected;
    private final FileDao fileDao = appContext.getBean(FileDao.class);

    @BeforeClass
    public static void beforeFileDaoTest() {
        creator.runScript("src/test/resources/insertestdb.sql");
        Document testDoc = new Document("test_doc");
        testDoc.getDocumentType().setCurentType("test_type");
        testDoc.setActualVersion(0);
        testDoc.getVersionsList().add(new DocumentVersion("test description", false));
        document_id = documentDao.getDocumentId(testDoc);
    }

    @AfterClass
    public static void afterFileDaoTest() {
        creator.runScript("src/test/resources/deletedb.sql");
    }

    private List<String> convertToStringList(List<DocumentFile> fileList) {
        List<String> list;
        list = new ArrayList<>();
        for (DocumentFile df : fileList) {
            list.add(df.getFileName());
        }
        return list;
    }

    @Before
    public void beforeEachFileDaoTest() {
        fileDao.addNewFile("test_file", document_id, "test_path");
        expected = convertToStringList(fileDao.getFilesList(document_id));
    }

    @After
    public void afterEachFileDaoTest() {
        fileDao.deleteFile("test_file");
    }

    @Test
    public void getFileListTest() {
        List<String> actual = convertToStringList(fileDao.getFilesList(document_id));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addFileTest() {
        expected.add("test_adding_file");
        fileDao.addNewFile("test_adding_file", document_id, "path_test");
        List<String> actual = convertToStringList(fileDao.getFilesList(document_id));
        fileDao.deleteFile("testing_adding_file");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void deleteFileTest() {
        expected.remove("test_file");
        fileDao.deleteFile("test_file");
        List<String> actual = convertToStringList(fileDao.getFilesList(document_id));
        fileDao.addNewFile("test_file", document_id, "test_path");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void loadFileTest() {
        DocumentFile file = fileDao.loadFile("test_file");
        Assert.assertEquals("test_path",file.getFilePath());
    }

}
