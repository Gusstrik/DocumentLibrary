import com.strelnikov.doclib.repository.DocFileDao;
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
public class DocFileDaoJdbcTest {
    private static int document_id;

    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(RepositoryConfiguration.class);
    private static final DatabaseCreatorJdbc creator = appContext.getBean(DatabaseCreatorJdbc.class);
    private List<String> expected;
    private final DocFileDao docFileDao = appContext.getBean(DocFileDao.class);
    private static DocumentVersion documentVersion = new DocumentVersion();

    @BeforeClass
    public static void beforeFileDaoTest() {
        creator.runScript("src/test/resources/insertestdb.sql");
        documentVersion.setVersion(0);
        documentVersion.setId(1);
    }

    @AfterClass
    public static void afterFileDaoTest() {
        creator.runScript("src/test/resources/deletedb.sql");
    }

    private List<String> convertToStringList() {
        List<String> list;
        list = new ArrayList<>();
        for (DocumentFile df : docFileDao.getFilesList(documentVersion)) {
            list.add(df.getFileName());
        }
        return list;
    }

    @Before
    public void beforeEachFileDaoTest() {
        expected = convertToStringList();
    }

    @Test
    public void insertFileTest() {
        expected.add("test_adding_file");
        DocumentFile documentFile= new DocumentFile("test_adding_file","test_path");
        documentFile.setDocVersionId(1);
        documentFile = docFileDao.insertFile(documentFile);
        List<String> actual = convertToStringList();
        docFileDao.deleteFile(documentFile.getId());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void deleteFileTest() {
        DocumentFile documentFile= new DocumentFile("test_adding_file","test_path");
        documentFile.setDocVersionId(1);
        documentFile = docFileDao.insertFile(documentFile);
        docFileDao.deleteFile(documentFile.getId());
        List<String> actual = convertToStringList();
        Assert.assertEquals(expected, actual);
    }
}
