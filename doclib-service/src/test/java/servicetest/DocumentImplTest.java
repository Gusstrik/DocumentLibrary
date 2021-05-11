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
        Assert.assertEquals("test description", documentDto.getVersionList().get(0).getDescription());
    }

    @Test
    public void addNewDocTest() throws VersionIsAlreadyExistException, UnitIsAlreadyExistException, UnitNotFoundException {
        Document document = new Document();
        document.setId(0);
        document.setName("test doc 2");
        DocumentType docType = new DocumentType();
        docType.setId(1);
        docType.setCurentType("test_type");
        document.setDocumentType(docType);
        document.setActualVersion(0);
        document.setCatalogId(1);
        document.setVersionsList(new ArrayList<>());
        document.setActualVersion(0);
        DocumentVersion docVersion = new DocumentVersion();
        docVersion.setId(0);
        docVersion.setParentDocument(document);
        docVersion.setVersion(0);
        docVersion.setDescription("testers");
        docVersion.setImportance(Importance.LOW);
        docVersion.setModerated(false);
        docVersion.setFilesList(new ArrayList<>());
        document.getVersionsList().add(docVersion);
        DocumentDto documentDto = dtoMapper.mapDocument(document);
        documentDto = documentActions.saveDocument(documentDto);
        documentDto = documentActions.loadDocument(documentDto.getId());
        int actual = documentDto.getVersionList().size();
        documentActions.deleteDocument(documentDto.getId());
        Assert.assertEquals(1, actual);
    }

    @Test
    public void editDocTest() throws VersionIsAlreadyExistException, UnitIsAlreadyExistException {
        Document document = new Document();
        document.setName("test doc 2");
        DocumentType docType = new DocumentType();
        docType.setId(1);
        docType.setCurentType("test_type");
        document.setDocumentType(docType);
        document.setId(0);
        document.setCatalogId(1);
        document.setVersionsList(new ArrayList<>());
        document.setActualVersion(0);
        DocumentVersion docVersion = new DocumentVersion();
        docVersion.setParentDocument(document);
        docVersion.setVersion(0);
        docVersion.setDescription("testers");
        docVersion.setImportance(Importance.LOW);
        docVersion.setModerated(false);
        docVersion.setFilesList(new ArrayList<>());
        document.getVersionsList().add(docVersion);
        DocumentDto documentDto = dtoMapper.mapDocument(document);
        documentDto = documentActions.saveDocument(documentDto);
        document = dtoMapper.mapDocument(documentDto);
        docVersion = new DocumentVersion();
        docVersion.setParentDocument(document);
        docVersion.setVersion(1);
        docVersion.setDescription("testers");
        docVersion.setImportance(Importance.LOW);
        docVersion.setModerated(false);
        docVersion.setFilesList(new ArrayList<>());
        documentDto.getVersionList().add(dtoMapper.mapDocVersion(docVersion));
        documentDto = documentActions.saveDocument(documentDto);
        int actual = documentDto.getVersionList().size();
        documentActions.deleteDocument(documentDto.getId());
        Assert.assertEquals(2, actual);
    }

    @Test
    public void deleteVerDocTest() throws VersionIsAlreadyExistException, UnitIsAlreadyExistException, UnitNotFoundException {
        Document document = new Document();
        document.setName("test doc 2");
        DocumentType docType = new DocumentType();
        docType.setId(1);
        docType.setCurentType("test_type");
        document.setDocumentType(docType);
        document.setId(0);
        document.setCatalogId(1);
        document.setVersionsList(new ArrayList<>());
        document.setActualVersion(0);
        DocumentVersion docVersion = new DocumentVersion();
        docVersion.setParentDocument(document);
        docVersion.setVersion(0);
        docVersion.setDescription("testers");
        docVersion.setImportance(Importance.LOW);
        docVersion.setModerated(false);
        docVersion.setFilesList(new ArrayList<>());
        document.getVersionsList().add(docVersion);
        DocumentDto documentDto = dtoMapper.mapDocument(document);
        documentDto = documentActions.saveDocument(documentDto);
        document = dtoMapper.mapDocument(documentDto);
        docVersion = new DocumentVersion();
        docVersion.setParentDocument(document);
        docVersion.setVersion(1);
        docVersion.setDescription("testers");
        docVersion.setImportance(Importance.LOW);
        docVersion.setModerated(false);
        docVersion.setFilesList(new ArrayList<>());
        documentDto.getVersionList().add(dtoMapper.mapDocVersion(docVersion));
        documentDto.getVersionList().remove(0);
        documentDto = documentActions.saveDocument(documentDto);
        documentDto = documentActions.loadDocument(documentDto.getId());
        int actual = documentDto.getVersionList().get(0).getVersion();
        documentActions.deleteDocument(documentDto.getId());
        Assert.assertEquals(1, actual);
    }

    @Test
    public void createAndDeleteDocVerTest() throws UnitNotFoundException, VersionIsAlreadyExistException, UnitIsAlreadyExistException {
        Document doc = dtoMapper.mapDocument(documentActions.loadDocument(1));
        DocumentVersion docVer = doc.getVersionsList().get(0);
        docVer.setId(0);
        docVer.setVersion(1);
        doc.getVersionsList().remove(0);
        doc.getVersionsList().add(docVer);
        DocumentDto docDto = documentActions.saveDocument(dtoMapper.mapDocument(doc));
        int actual = docDto.getVersionList().get(0).getVersion();
        Assert.assertEquals(1,actual);
    }

}
