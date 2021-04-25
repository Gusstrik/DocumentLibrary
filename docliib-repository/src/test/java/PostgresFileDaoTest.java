import com.strelnikov.doclib.postgresdatabase.*;

import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class PostgresFileDaoTest {
    private static final Logger log;

    static {
        log = LoggerFactory.getLogger("PostgresTest");
    }

    private static int document_id;

    @BeforeClass
    public static void beforeFileDaoTest() {
        TypeDao typeDao = new TypeDao();
        typeDao.addType("test_type");
        DocumentDao documentDao = new DocumentDao();
        documentDao.addNewDocuemnt("test_doc", "test_type", 1);
        document_id = documentDao.getDocumentId("test_doc", "test_type", 1);
    }

    @AfterClass
    public static void afterFileDaoTest() {
        TypeDao typeDao = new TypeDao();
        typeDao.deleteType("test_type");
        DocumentDao documentDao = new DocumentDao();
        documentDao.deleteDocument(document_id);
    }

    private ArrayList<String> expected;

    @Before
    public void beforeEachFileDaoTest() {
        FileDao fileDao = new FileDao();
        fileDao.addNewFile("test_file", document_id, "test_path");
        expected = fileDao.getFilesList(document_id);
    }

    @After
    public void afterEachFileDaoTest() {
        FileDao fileDao = new FileDao();
        fileDao.deleteFile("test_file");
    }

    @Test
    public void getFileListTest() {
        FileDao fileDao = new FileDao();
        ArrayList<String> actual = fileDao.getFilesList(document_id);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addFileTest() {
        FileDao fileDao = new FileDao();
        expected.add("test_adding_file");
        fileDao.addNewFile("test_adding_file", document_id, "path_test");
        ArrayList<String> actual = fileDao.getFilesList(document_id);
        fileDao.deleteFile("testing_adding_file");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void deleteFileTest() {
        FileDao fileDao = new FileDao();
        expected.remove("test_file");
        fileDao.deleteFile("test_file");
        ArrayList<String> actual = fileDao.getFilesList(document_id);
        fileDao.addNewFile("test_file", document_id, "test_path");
        Assert.assertEquals(expected, actual);
    }

}
