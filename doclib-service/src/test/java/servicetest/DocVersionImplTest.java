package servicetest;

import com.strelnikov.doclib.dto.DocVersionDto;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentVersion;
import com.strelnikov.doclib.repository.DocVersionDao;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.service.DocTypeActions;
import com.strelnikov.doclib.service.DocVersionActions;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.service.impl.configuration.ServiceImplConfiguration;
import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class DocVersionImplTest {
    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(ServiceImplConfiguration.class, RepositoryConfiguration.class);

    private static final DatabaseCreatorJdbc creator = appContext.getBean(DatabaseCreatorJdbc.class);
    private final DocVersionActions docVerActions = appContext.getBean(DocVersionActions.class);
    private final DtoMapper dtoMapper = appContext.getBean(DtoMapper.class);
    private final DocVersionDao docVerDao = appContext.getBean(DocVersionDao.class);

    private int expected;
    private static Document document;

    @BeforeClass
    public static void beforCataloImpTests() {
        creator.runScript("src/test/resources/insertestdb.sql");
        document = new Document();
        document.setId(1);
    }

    @AfterClass
    public static void afterCatalogImpTests() {
        creator.runScript("src/test/resources/deletedb.sql");
    }

    @Before
    public void beforeEachDocVersionImplTest() {
        expected = docVerDao.getDocVersionList(document).size();
    }

    @Test
    public void saveDocVersionTest() {
        DocumentVersion docVersion = docVerDao.getDocVersionList(document).get(0);
        docVersion.setVersion(1);
        docVersion.setDescription("another test version");
        DocVersionDto docVerDto = docVerActions.saveDocVersion(dtoMapper.mapDocVersion(docVersion));
        int actual = docVerDao.getDocVersionList(document).size();
        docVerActions.deleteDocVersion(docVerDto.getId());
        expected++;
        Assert.assertEquals(expected, actual);
    }
    @Test
    public void deleteDocVersionTest() {
        DocumentVersion docVersion = docVerDao.getDocVersionList(document).get(0);
        docVersion.setVersion(1);
        docVersion.setDescription("another test version");
        DocVersionDto docVerDto = docVerActions.saveDocVersion(dtoMapper.mapDocVersion(docVersion));
        docVerActions.deleteDocVersion(docVerDto.getId());
        int actual = docVerDao.getDocVersionList(document).size();
        Assert.assertEquals(expected, actual);
    }
}
