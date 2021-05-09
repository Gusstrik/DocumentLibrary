
import com.strelnikov.doclib.model.documnets.DocumentType;
import com.strelnikov.doclib.repository.DocTypeDao;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DocTypeDaoTest {

    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(RepositoryConfiguration.class);
    private final DocTypeDao docTypeDao = appContext.getBean("DocTypeJpa", DocTypeDao.class);
    private List<String> expected;

    private static final DatabaseCreatorJdbc creator = appContext.getBean(DatabaseCreatorJdbc.class);

    @BeforeClass
    public static void beforeFileDaoTest() {
        creator.runScript("src/test/resources/insertestdb.sql");
    }

    @AfterClass
    public static void afterFileDaoTest() {
        creator.runScript("src/test/resources/deletedb.sql");
    }

    private List<String> convertToString(){
        List<DocumentType> typeList = docTypeDao.getTypesList();
        List<String> list = new ArrayList<>();
        for(DocumentType docType: typeList){
            list.add(docType.getCurentType());
        }
        return list;
    }


    @Before
    public void beforeEachTypeDaoTest() {
        expected = convertToString();
    }


    @Test
    public void getTypeListTest() {
        List<String> actual = convertToString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addTypeTest() {
        DocumentType docType = new DocumentType();
        docType.setCurentType("test_adding_type");
        docType = docTypeDao.insertType(docType);
        expected.add("test_adding_type");
        List<String> actual = convertToString();
        docTypeDao.deleteType(docType.getId());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void deleteTypeTest() {
        DocumentType docType = new DocumentType();
        docType.setCurentType("test_adding_type");
        docType = docTypeDao.insertType(docType);
        docTypeDao.deleteType(docType.getId());
        List<String> actual = convertToString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void loadTypeTest(){
        DocumentType docType = docTypeDao.loadType(1);
        Assert.assertEquals("test_type",docType.getCurentType());
    }

}
