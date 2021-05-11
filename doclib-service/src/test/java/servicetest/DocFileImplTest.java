package servicetest;

import com.strelnikov.doclib.dto.DocFileDto;
import com.strelnikov.doclib.dto.DocVersionDto;
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
        DocFileDto fileDto = new DocFileDto(0,"test_file","test_path");
        fileDto = fileActions.createNewFile(fileDto);
        List<DocFileDto> fileList = new ArrayList<>();
        fileList.add(fileDto);
        DocVersionDto docVersionDto = new DocVersionDto(0,1,1,"test_ver",
                Importance.LOW.toString(),false,fileList);
        docVersionDto = versionActions.saveDocVersion(docVersionDto);
        int actual = docFileDao.getFilesList(dtoMapper.mapDocVersion(docVersionDto)).size();
        fileActions.deleteFile(fileDto.getId());
        versionActions.deleteDocVersion(docVersionDto.getId());
        Assert.assertEquals(1,actual);
    }

    @Test
    public void fileDeleteTest() throws VersionIsAlreadyExistException, UnitNotFoundException, FileIsAlreadyExistException {
        DocFileDto fileDto = new DocFileDto(0,"test_file","test_path");
        fileDto = fileActions.createNewFile(fileDto);
        List<DocFileDto> fileList = new ArrayList<>();
        fileList.add(fileDto);
        DocVersionDto docVersionDto = new DocVersionDto(0,1,1,"test_ver",
                Importance.LOW.toString(),false,fileList);
        docVersionDto = versionActions.saveDocVersion(docVersionDto);
        fileActions.deleteFile(fileDto.getId());
        int actual = docFileDao.getFilesList(dtoMapper.mapDocVersion(docVersionDto)).size();
        versionActions.deleteDocVersion(docVersionDto.getId());
        Assert.assertEquals(0,actual);
    }


}
