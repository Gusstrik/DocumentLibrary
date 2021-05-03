import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.model.documnets.DocumentType;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import com.strelnikov.doclib.service.DocumentTypeActions;
import com.strelnikov.doclib.service.impl.configuration.ServiceImplConfiguration;
import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class DocumentTypeImplTest {
    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(ServiceImplConfiguration.class, RepositoryConfiguration.class);

    private static final DatabaseCreatorJdbc creator = appContext.getBean(DatabaseCreatorJdbc.class);
    private final DocumentTypeActions documentTypeActions = appContext.getBean(DocumentTypeActions.class);

    private List<String>  expected;

    @BeforeClass
    public static void beforCataloImpTests() {
        creator.runScript("src/test/resources/insertestdb.sql");
    }

    @AfterClass
    public static void afterCatalogImpTests() {
        creator.runScript("src/test/resources/deletedb.sql");
    }



    @Before
    public void beforeEachDocumentTypeImplTest(){
        documentTypeActions.refreshListDocumentType();
        expected=new ArrayList<>(DocumentType.documentTypeList);
    }

    @Test
    public void addDocumentTypeTest(){
        documentTypeActions.addDocumentType("another_test_type");
        expected.add("another_test_type");
        documentTypeActions.refreshListDocumentType();
        List<String> actual = new ArrayList<>(DocumentType.documentTypeList);
        documentTypeActions.deleteDocumentType("another_test_type");
        Assert.assertEquals(expected,actual);
    }
    @Test
    public void deleteDocumentTypeTest(){
        documentTypeActions.deleteDocumentType("test_type");
        expected.remove("test_type");
        documentTypeActions.refreshListDocumentType();
        List<String> actual = new ArrayList<>(DocumentType.documentTypeList);
        documentTypeActions.addDocumentType("test_type");
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void refreshDocumentTypeListTest(){
        List<String> expected = new ArrayList<>();
        expected.add("test_type");
        documentTypeActions.refreshListDocumentType();
        Assert.assertEquals(expected,DocumentType.documentTypeList);
    }
}
