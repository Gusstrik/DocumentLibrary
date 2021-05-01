import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.model.documnets.DocumentType;
import com.strelnikov.doclib.service.DocumentTypeActions;
import com.strelnikov.doclib.service.impl.DocumentTypeImpl;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

public class DocumentTypeImplTest {
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

    private DocumentType documentType = new DocumentType();
    private List<String>  expected;

    @Before
    public void beforeEachDocumentTypeImplTest(){
        DocumentTypeActions documentTypeActions= new DocumentTypeImpl();
        documentTypeActions.refreshListDocumentType();
        expected=new ArrayList(DocumentType.documentTypeList);
    }

    @Test
    public void addDocumentTypeTest(){
        DocumentTypeActions documentTypeActions = new DocumentTypeImpl();
        documentTypeActions.addDocumentType("another_test_type");
        expected.add("another_test_type");
        documentTypeActions.refreshListDocumentType();
        List<String> actual = new ArrayList(DocumentType.documentTypeList);
        documentTypeActions.deleteDocumentType("another_test_type");
        Assert.assertEquals(expected,actual);
    }
    @Test
    public void deleteDocumentTypeTest(){
        DocumentTypeActions documentTypeActions = new DocumentTypeImpl();
        documentTypeActions.deleteDocumentType("test_type");
        expected.remove("test_type");
        documentTypeActions.refreshListDocumentType();
        List<String> actual = new ArrayList(DocumentType.documentTypeList);
        documentTypeActions.addDocumentType("test_type");
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void refreshDocumentTypeListTest(){
        List<String> expected = new ArrayList();
        expected.add("test_type");
        DocumentTypeActions documentTypeActions = new DocumentTypeImpl();
        documentTypeActions.refreshListDocumentType();
        Assert.assertEquals(expected,DocumentType.documentTypeList);
    }
}
