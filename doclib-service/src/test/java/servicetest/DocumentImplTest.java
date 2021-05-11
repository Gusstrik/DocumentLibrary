package servicetest;

import com.strelnikov.doclib.dto.DocumentDto;
import com.strelnikov.doclib.model.documnets.Importance;
import com.strelnikov.doclib.repository.DocumentDao;
import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentType;
import com.strelnikov.doclib.model.documnets.DocumentVersion;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import com.strelnikov.doclib.service.CatalogActions;
import com.strelnikov.doclib.service.DocumentActions;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.service.exceptions.UnitIsAlreadyExistException;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import com.strelnikov.doclib.service.exceptions.VersionIsAlreadyExistException;
import com.strelnikov.doclib.service.impl.configuration.ServiceImplConfiguration;
import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class DocumentImplTest {
    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(ServiceImplConfiguration.class, RepositoryConfiguration.class);

    private static final DatabaseCreatorJdbc creator = appContext.getBean(DatabaseCreatorJdbc.class);
    private final DocumentActions documentActions = appContext.getBean(DocumentActions.class);
    private final DtoMapper dtoMapper = appContext.getBean(DtoMapper.class);


    @BeforeClass
    public static void beforCataloImpTests() {
        creator.runScript("src/test/resources/insertestdb.sql");
    }

    @AfterClass
    public static void afterCatalogImpTests() {
        creator.runScript("src/test/resources/deletedb.sql");
    }

    @Before
    public void beforeEachDocumentImlTest() {

    }

    @Test
    public void loadDocTest() throws UnitNotFoundException {
        DocumentDto documentDto = documentActions.loadDocument(1);
        Assert.assertEquals("test description", documentDto.getVersion().getDescription());
    }

    @Test
    public void addNewDocTest() throws VersionIsAlreadyExistException, UnitIsAlreadyExistException, UnitNotFoundException {
        DocumentDto documentDto = documentActions.loadDocument(1);
        Document document = dtoMapper.mapDocument(documentDto);
        document.setId(0);
        document.setName("test_doc2");
        document.getVersionsList().get(0).setId(0);
        document.getVersionsList().get(0).setParentDocument(document);
        documentDto = dtoMapper.mapDocument(document);
        documentDto = documentActions.saveDocument(documentDto);
        documentDto = documentActions.loadDocument(documentDto.getId());
        int actual = documentDto.getVersion().getVersion();
        documentActions.deleteDocument(documentDto.getId());
        Assert.assertEquals(0, actual);
    }

    @Test
    public void editDocTest() throws VersionIsAlreadyExistException, UnitIsAlreadyExistException, UnitNotFoundException {
        DocumentDto documentDto = documentActions.loadDocument(1);
        Document document = dtoMapper.mapDocument(documentDto);
        document.setName("edit test");
        documentDto = dtoMapper.mapDocument(document);
        documentDto = documentActions.saveDocument(documentDto);
        String actual = documentActions.loadDocument(1).getName();
        document = dtoMapper.mapDocument(documentDto);
        document.setName("test_doc");
        documentDto = dtoMapper.mapDocument(document);
        documentDto = documentActions.saveDocument(documentDto);
        Assert.assertEquals("edit test", actual);
    }

    @Test
    public void editDocVerTest() throws UnitNotFoundException, VersionIsAlreadyExistException, UnitIsAlreadyExistException {
        DocumentDto documentDto = documentActions.loadDocument(1);
        Document document = dtoMapper.mapDocument(documentDto);
        document.setActualVersion(1);
        DocumentVersion documentVersion = document.getVersionsList().get(0);
        documentVersion.setVersion(1);
        documentVersion.setId(0);
        documentVersion.setDescription("added version");
        document.getVersionsList().add(documentVersion);
        documentDto = dtoMapper.mapDocument(document);
        documentDto = documentActions.saveDocument(documentDto);
        documentDto = documentActions.loadDocument(documentDto.getId());
        String actual = documentDto.getVersion().getDescription();
        documentActions.rollback(documentDto.getId(),0);
        Assert.assertEquals("added version",actual );
    }
    @Test
    public void rollbackDocTest() throws UnitNotFoundException, VersionIsAlreadyExistException, UnitIsAlreadyExistException {
        DocumentDto documentDto = documentActions.loadDocument(1);
        Document document = dtoMapper.mapDocument(documentDto);
        document.setActualVersion(1);
        DocumentVersion documentVersion = document.getVersionsList().get(0);
        documentVersion.setVersion(1);
        documentVersion.setId(0);
        documentVersion.setDescription("added version");
        document.getVersionsList().add(documentVersion);
        documentDto = dtoMapper.mapDocument(document);
        documentDto = documentActions.saveDocument(documentDto);
        documentActions.rollback(documentDto.getId(),0);
        documentDto = documentActions.loadDocument(documentDto.getId());
        String actual = documentDto.getVersion().getDescription();
        Assert.assertEquals("test description",actual );
    }
}
