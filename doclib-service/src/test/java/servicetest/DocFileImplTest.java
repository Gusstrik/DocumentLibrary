package servicetest;

import com.strelnikov.doclib.dto.DocFileDto;
import com.strelnikov.doclib.dto.DocVersionDto;
import com.strelnikov.doclib.model.documnets.DocumentFile;
import com.strelnikov.doclib.model.documnets.DocumentVersion;
import com.strelnikov.doclib.model.documnets.Importance;
import com.strelnikov.doclib.repository.DocFileDao;
import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import com.strelnikov.doclib.service.DocVersionActions;
import com.strelnikov.doclib.service.DocumentActions;
import com.strelnikov.doclib.service.DocFileActions;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.service.exceptions.FileIsAlreadyExistException;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import com.strelnikov.doclib.service.exceptions.VersionIsAlreadyExistException;
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
    private final DocVersionActions versionActions = appContext.getBean(DocVersionActions.class);
    private final DtoMapper dtoMapper =appContext.getBean(DtoMapper.class);
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
    public void fileAddTest() throws VersionIsAlreadyExistException, UnitNotFoundException, FileIsAlreadyExistException {
        DocFileDto fileDto = new DocFileDto(0,"test_add","test_path");
        fileDto = fileActions.createNewFile(fileDto);
        String actual = fileActions.loadFile("test_add").getPath();
        fileActions.deleteFile(fileDto.getId());
        Assert.assertEquals("test_path",actual);
    }

    @Test
    public void fileDeleteTest() throws VersionIsAlreadyExistException, UnitNotFoundException, FileIsAlreadyExistException {
        DocFileDto fileDto = new DocFileDto(0,"test_add","test_path");
        fileDto = fileActions.createNewFile(fileDto);
        fileActions.deleteFile(fileDto.getId());
        Assert.assertEquals(false,fileActions.isFileExist(dtoMapper.mapDocFile(fileDto)));
    }

    @Test
    public void loadFileTest() throws UnitNotFoundException {
        DocFileDto fileDto = fileActions.loadFile("test_file");
        Assert.assertEquals(1,fileDto.getId());
    }

    @Test
    public void isFileExistTest() {
        DocumentFile docFile = new DocumentFile();
        docFile.setId(1);
        docFile.setFileName("test_file");
        docFile.setFilePath("test_path");
        Assert.assertEquals(true,fileActions.isFileExist(docFile));
    }


}
