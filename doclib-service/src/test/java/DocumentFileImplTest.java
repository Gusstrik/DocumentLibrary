import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentFile;
import com.strelnikov.doclib.model.documnets.DocumentType;
import com.strelnikov.doclib.service.DocumentActions;
import com.strelnikov.doclib.service.DocumentFileActions;
import com.strelnikov.doclib.service.impl.DocumentFileImpl;
import com.strelnikov.doclib.service.impl.DocumentImpl;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

public class DocumentFileImplTest {
    @BeforeClass
    public static void beforCataloImpTests() {
        DatabaseCreatorJdbc creator = new DatabaseCreatorJdbc();
        creator.runScript("src/test/resources/insertestdb.sql");
    }

    @AfterClass
    public static void afterCatalogImpTests() {
        DatabaseCreatorJdbc creator = new DatabaseCreatorJdbc();
        creator.runScript("src/test/resources/deletedb.sql");
    }

    private List<String> convertToStringList(List<DocumentFile> fileList){
        List<String> list = new ArrayList();
        for (DocumentFile file:fileList){
            list.add(file.getFileName());
        }
        return list;
    }

    private List<String> expected;
    private final DocumentFileActions fileActions = new DocumentFileImpl();
    private final  DocumentActions documentActions = new DocumentImpl();
    private Document document;


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
