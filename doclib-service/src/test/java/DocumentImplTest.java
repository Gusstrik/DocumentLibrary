import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentType;
import com.strelnikov.doclib.model.documnets.DocumentVersion;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import com.strelnikov.doclib.service.CatalogActions;
import com.strelnikov.doclib.service.DocumentActions;
import com.strelnikov.doclib.service.impl.configuration.ServiceImplConfiguration;
import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class DocumentImplTest {
    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(ServiceImplConfiguration.class, RepositoryConfiguration.class);

    private static final DatabaseCreatorJdbc creator = appContext.getBean(DatabaseCreatorJdbc.class);
    private final DocumentActions documentActions = appContext.getBean(DocumentActions.class);
    private final CatalogActions catalogActions = appContext.getBean(CatalogActions.class);

    private List<String> expected;
    private final Catalog mainCatalog = new Catalog("/");

    @BeforeClass
    public static void beforCataloImpTests() {
        creator.runScript("src/test/resources/insertestdb.sql");
    }

    @AfterClass
    public static void afterCatalogImpTests() {
        creator.runScript("src/test/resources/deletedb.sql");
    }

    @Before
    public void beforeEachDocumentImlTest(){
        expected=catalogActions.showContent(mainCatalog);
    }

    @Test
    public void createNewDocumentTest() {
        Document document = documentActions.createNewDocument("test_doc2",new DocumentType("test_type"),mainCatalog);
        List<String> actual = catalogActions.showContent(mainCatalog);
        expected.add("test_doc2");
        documentActions.deleteDocument(document);
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void deleteDocumentVersionTest() {
        Catalog catalog = new Catalog("test_parent");
        Document document = documentActions.loadDocument("test_doc",new DocumentType("test_type"));
        document.getVersionsList().add(new DocumentVersion());
        document.setActualVersion(document.getActualVersion()+1);
        DocumentVersion docVersion = document.getDocumentVersion();
        docVersion.setDescription("Another created test version of doc");
        documentActions.createNewDocumentVersion(document,new Catalog("test_parent"));
        expected=catalogActions.showContent(catalog);
        expected.remove("test_doc");
        documentActions.deleteDocumentVersion(document);
        List<String> actual = catalogActions.showContent(catalog);
        documentActions.createNewDocumentVersion(document,catalog);
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void loadDocumentTest(){
        Document document = documentActions.loadDocument("test_doc",new DocumentType("test_type"));
        Assert.assertEquals(0, document.getActualVersion());
    }

   @Test
   public void createNewDocumentVersionTest() {
       Document document = documentActions.loadDocument("test_doc",new DocumentType("test_type"));
       document.getVersionsList().add(new DocumentVersion());
       document.setActualVersion(document.getActualVersion()+1);
       DocumentVersion docVersion = document.getDocumentVersion();
       docVersion.setDescription("Another created test version of doc");
       documentActions.createNewDocumentVersion(document,new Catalog("test_parent"));
       document = documentActions.loadDocument("test_doc", new DocumentType("test_type"));
       String actual = document.getDocumentVersion().getDescription();
       documentActions.deleteDocumentVersion(document);
       Assert.assertEquals("Another created test version of doc", actual);
   }

   @Test
    public void deleteNonActualVersionsTest(){
       Document document = documentActions.loadDocument("test_doc",new DocumentType("test_type"));
       document.getVersionsList().add(new DocumentVersion());
       document.setActualVersion(document.getActualVersion());
       DocumentVersion docVersion = document.getDocumentVersion();
       docVersion.setDescription("Another created test version of doc");
       documentActions.createNewDocumentVersion(document,new Catalog("test_parent"));
       document=documentActions.deleteNotActualVersions(document);
       Assert.assertEquals(1,document.getActualVersion());
   }

   @Test
    public void deleteDocumentTest(){
        Document document = documentActions.loadDocument("test_doc",new DocumentType("test_type"));
       Catalog catalog = catalogActions.loadCatalog("test_parent");
       List<String>expected = catalogActions.showContent(catalog);
       expected.remove("test_doc");
       documentActions.deleteDocument(document);
        List<String> actual = catalogActions.showContent(catalog);
        documentActions.createNewDocument(document.getName(), document.getDocumentType(), catalog);
        Assert.assertEquals(expected,actual);
   }

}
