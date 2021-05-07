package servicetest;

import com.strelnikov.doclib.dto.DocFileDto;
import com.strelnikov.doclib.model.documnets.DocumentVersion;
import com.strelnikov.doclib.repository.DocFileDao;
import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import com.strelnikov.doclib.service.DocumentActions;
import com.strelnikov.doclib.service.DocFileActions;
import com.strelnikov.doclib.service.impl.configuration.ServiceImplConfiguration;
import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class DocFileImplTest {
    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(ServiceImplConfiguration.class);

    private static final DatabaseCreatorJdbc creator = appContext.getBean(DatabaseCreatorJdbc.class);
    private final DocFileDao docFileDao = appContext.getBean("DocFileJpa",DocFileDao.class);
    private final DocFileActions fileActions = appContext.getBean(DocFileActions.class);
    private int expected;
    private  static DocumentVersion docVersion;

    @BeforeClass
    public static void beforeCatalogImpTests() {
        docVersion=new DocumentVersion();
        docVersion.setId(1);
        creator.runScript("src/test/resources/insertestdb.sql");
    }

    @AfterClass
    public static void afterCatalogImpTests() {
        creator.runScript("src/test/resources/deletedb.sql");
    }

    @Before
    public void beforeEachFileImplTest(){
        expected=docFileDao.getFilesList(docVersion).size();
    }

    @Test
    public void fileAddTest(){
        DocFileDto fileDto = new DocFileDto(0,1,"test_file","test_path");
        fileDto = fileActions.createNewFile(fileDto);
        int actual = docFileDao.getFilesList(docVersion).size();
        fileActions.deleteFile(fileDto);
        expected++;
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void fileDeleteTest(){
        DocFileDto fileDto = new DocFileDto(0,1,"test_file","test_path");
        fileDto = fileActions.createNewFile(fileDto);
        fileActions.deleteFile(fileDto);
        int actual = docFileDao.getFilesList(docVersion).size();
        Assert.assertEquals(expected,actual);
    }


}
