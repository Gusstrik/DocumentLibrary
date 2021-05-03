import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentFile;
import com.strelnikov.doclib.model.documnets.DocumentType;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import com.strelnikov.doclib.service.DocumentActions;
import com.strelnikov.doclib.service.DocumentFileActions;
import com.strelnikov.doclib.service.impl.configuration.ServiceImplConfiguration;
import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class DocumentFileImplTest {
    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(ServiceImplConfiguration.class, RepositoryConfiguration.class);

    private static final DatabaseCreatorJdbc creator = appContext.getBean(DatabaseCreatorJdbc.class);
    private final DocumentActions documentActions = appContext.getBean(DocumentActions.class);
    private final DocumentFileActions fileActions = appContext.getBean(DocumentFileActions.class);
    private List<String> expected;
    private Document document;

    @BeforeClass
    public static void beforeCatalogImpTests() {
        creator.runScript("src/test/resources/insertestdb.sql");
    }

    @AfterClass
    public static void afterCatalogImpTests() {
        creator.runScript("src/test/resources/deletedb.sql");
    }

    private List<String> convertToStringList(List<DocumentFile> fileList){
        List<String> list;
        list = new ArrayList<>();
        for (DocumentFile file:fileList){
            list.add(file.getFileName());
        }
        return list;
    }

    @Before
    public void beforeEachFileTest(){
        document = documentActions.loadDocument("tes" +
                "t_doc",new DocumentType("test_type"));
        documentActions.refreshDocumentsFileList(document);
        expected = convertToStringList(document.getDocumentVersion().getFilesList());
    }
    @Test
    public void addFileTest(){
        fileActions.createNewFile(new DocumentFile("test_file2","path"),document);
        documentActions.refreshDocumentsFileList(document);
        List<String> actual = convertToStringList(document.getDocumentVersion().getFilesList());
        expected.add("test_file2");
        fileActions.deleteFile(new DocumentFile("test_file2","path"));
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void deleteFileTest(){
        DocumentFile file = fileActions.loadFile("test_file");
        fileActions.deleteFile(file);
        documentActions.refreshDocumentsFileList(document);
        expected.remove("test_file");
        List<String> actual = convertToStringList(document.getDocumentVersion().getFilesList());
        fileActions.createNewFile(file,document);
        Assert.assertEquals(expected,actual);
    }
    @Test
    public  void loadFileTest(){
        DocumentFile file = fileActions.loadFile("test_file");
        Assert.assertEquals("test_path",file.getFilePath());
    }
}
