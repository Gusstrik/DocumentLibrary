package servicetest;

import com.strelnikov.doclib.dto.DocTypeDto;
import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.model.documnets.DocumentType;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import com.strelnikov.doclib.service.DocTypeActions;
import com.strelnikov.doclib.service.impl.configuration.ServiceImplConfiguration;
import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class DocTypeImplTest {
    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(ServiceImplConfiguration.class, RepositoryConfiguration.class);

    private static final DatabaseCreatorJdbc creator = appContext.getBean(DatabaseCreatorJdbc.class);
    private final DocTypeActions docTypeActions = appContext.getBean(DocTypeActions.class);

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
        docTypeActions.refreshListDocumentType();
        expected=DocumentType.documentTypeList;
    }

    @Test
    public void addDocumentTypeTest(){
        DocTypeDto typeDto = new DocTypeDto("test_type2");
        docTypeActions.addDocumentType(typeDto);
        docTypeActions.refreshListDocumentType();
        List<String> actual = DocumentType.documentTypeList;
        expected.add("test_type2");
        docTypeActions.deleteDocumentType(typeDto);
        Assert.assertEquals(expected,actual);
    }
    @Test
    public void deleteDocumentTypeTest(){
        DocTypeDto typeDto = new DocTypeDto("test_type2");
        docTypeActions.addDocumentType(typeDto);
        docTypeActions.deleteDocumentType(typeDto);
        docTypeActions.refreshListDocumentType();
        List<String> actual = DocumentType.documentTypeList;
        Assert.assertEquals(expected,actual);
    }

}
