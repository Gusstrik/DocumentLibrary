package servicetest;

import com.strelnikov.doclib.dto.DocTypeDto;
import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.model.documnets.DocumentType;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import com.strelnikov.doclib.service.DocTypeActions;
import com.strelnikov.doclib.service.exceptions.TypeIsAlreadyExistException;
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
        expected=convertToString();


    }
    private List<String> convertToString(){
        List<DocumentType> typeList = DocumentType.documentTypeList;
        List<String> list = new ArrayList<>();
        for(DocumentType docType: typeList){
            list.add(docType.getCurentType());
        }
        return list;
    }

    @Test
    public void addDocumentTypeTest() throws TypeIsAlreadyExistException {
        DocTypeDto typeDto = new DocTypeDto(0,"test_type2");
        typeDto = docTypeActions.addDocumentType(typeDto);
        docTypeActions.refreshListDocumentType();
        List<String> actual = convertToString();
        expected.add("test_type2");
        docTypeActions.deleteDocumentType(typeDto.getId());
        Assert.assertEquals(expected,actual);
    }
    @Test
    public void deleteDocumentTypeTest() throws TypeIsAlreadyExistException {
        DocTypeDto typeDto = new DocTypeDto(0,"test_type2");
        typeDto=docTypeActions.addDocumentType(typeDto);
        docTypeActions.deleteDocumentType(typeDto.getId());
        docTypeActions.refreshListDocumentType();
        List<String> actual = convertToString();
        Assert.assertEquals(expected,actual);
    }

}
