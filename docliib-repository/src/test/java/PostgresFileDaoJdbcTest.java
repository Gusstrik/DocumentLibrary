import com.strelnikov.doclib.database.jdbc.DocumentDaoJdbc;
import com.strelnikov.doclib.database.jdbc.FileDaoJdbc;
import com.strelnikov.doclib.database.jdbc.TypeDaoJdbc;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class PostgresFileDaoJdbcTest {
    private static final Logger log;

    static {
        log = LoggerFactory.getLogger("PostgresTest");
    }

    private static int document_id;

    @BeforeClass
    public static void beforeFileDaoTest() {
        TypeDaoJdbc typeDaoJdbc = new TypeDaoJdbc();
        typeDaoJdbc.addType("test_type");
        DocumentDaoJdbc documentDaoJdbc = new DocumentDaoJdbc();
        documentDaoJdbc.addNewDocuemnt("test_doc", "test_type", 1);
        document_id = documentDaoJdbc.getDocumentId("test_doc", "test_type", 1);
    }

    @AfterClass
    public static void afterFileDaoTest() {
        TypeDaoJdbc typeDaoJdbc = new TypeDaoJdbc();
        typeDaoJdbc.deleteType("test_type");
        DocumentDaoJdbc documentDaoJdbc = new DocumentDaoJdbc();
        documentDaoJdbc.deleteDocument(document_id);
    }

    private ArrayList<String> expected;

    @Before
    public void beforeEachFileDaoTest() {
        FileDaoJdbc fileDaoJdbc = new FileDaoJdbc();
        fileDaoJdbc.addNewFile("test_file", document_id, "test_path");
        expected = fileDaoJdbc.getFilesList(document_id);
    }

    @After
    public void afterEachFileDaoTest() {
        FileDaoJdbc fileDaoJdbc = new FileDaoJdbc();
        fileDaoJdbc.deleteFile("test_file");
    }

    @Test
    public void getFileListTest() {
        FileDaoJdbc fileDaoJdbc = new FileDaoJdbc();
        ArrayList<String> actual = fileDaoJdbc.getFilesList(document_id);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addFileTest() {
        FileDaoJdbc fileDaoJdbc = new FileDaoJdbc();
        expected.add("test_adding_file");
        fileDaoJdbc.addNewFile("test_adding_file", document_id, "path_test");
        ArrayList<String> actual = fileDaoJdbc.getFilesList(document_id);
        fileDaoJdbc.deleteFile("testing_adding_file");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void deleteFileTest() {
        FileDaoJdbc fileDaoJdbc = new FileDaoJdbc();
        expected.remove("test_file");
        fileDaoJdbc.deleteFile("test_file");
        ArrayList<String> actual = fileDaoJdbc.getFilesList(document_id);
        fileDaoJdbc.addNewFile("test_file", document_id, "test_path");
        Assert.assertEquals(expected, actual);
    }

}
