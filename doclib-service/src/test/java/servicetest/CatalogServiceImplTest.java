package servicetest;

import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.service.dtomapper.configuration.DtoMapperConfiguration;
import com.strelnikov.doclib.service.exceptions.CannotDeleteMainCatalogException;
import com.strelnikov.doclib.service.exceptions.UnitIsAlreadyExistException;
import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import com.strelnikov.doclib.service.CatalogService;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import com.strelnikov.doclib.service.impl.configuration.ServiceImplConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class CatalogServiceImplTest {

    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(ServiceImplConfiguration.class,
            RepositoryConfiguration.class, DtoMapperConfiguration.class);

    private static final DatabaseCreatorJdbc creator = appContext.getBean(DatabaseCreatorJdbc.class);
    private final CatalogService catalogAction = appContext.getBean(CatalogService.class);
    private final DtoMapper dtoMapper = appContext.getBean(DtoMapper.class);

    @BeforeClass
    public static void beforeCatalogImpTests() {
        creator.runScript("src/test/resources/insertestdb.sql");
    }

    @AfterClass
    public static void afterCatalogImpTests() {
        creator.runScript("src/test/resources/deletedb.sql");
    }

    @Test
    public void saveCatalogTest() throws UnitIsAlreadyExistException, UnitNotFoundException, CannotDeleteMainCatalogException {
        CatalogDto mainCat = catalogAction.loadCatalog(1);
        int expected = mainCat.getContentList().size();
        Catalog catalog = dtoMapper.mapCatalog(mainCat);
        catalog.setId(0);
        catalog.setName("test_catalog1");
        catalog.setCatalogId(1);
        CatalogDto catalogDto = catalogAction.saveCatalog(dtoMapper.mapCatalog(catalog));
        expected += 1;
        mainCat = catalogAction.loadCatalog(1);
        int actual = mainCat.getContentList().size();
        catalogAction.deleteCatalog(catalogDto);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void deleteCatalogTest() throws UnitNotFoundException, UnitIsAlreadyExistException, CannotDeleteMainCatalogException {
        CatalogDto mainCat = catalogAction.loadCatalog(1);
        int expected = mainCat.getContentList().size();
        Catalog catalog = dtoMapper.mapCatalog(mainCat);
        catalog.setName("test catalog1");
        catalog.setId(0);
        catalog.setCatalogId(1);
        CatalogDto catalogDto = catalogAction.saveCatalog(dtoMapper.mapCatalog(catalog));
        catalogAction.deleteCatalog(catalogDto);
        mainCat = catalogAction.loadCatalog(1);
        int actual = mainCat.getContentList().size();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void loadCatalogTest() throws UnitNotFoundException {
        CatalogDto catalogDto = catalogAction.loadCatalog(1);
        Assert.assertEquals("/", catalogDto.getName());
    }

    @Test
    public void editCatalogTest() throws UnitIsAlreadyExistException, UnitNotFoundException, CannotDeleteMainCatalogException {
        CatalogDto mainCat = catalogAction.loadCatalog(1);
        Catalog testCat1 = dtoMapper.mapCatalog(mainCat);
        testCat1.setId(0);
        testCat1.setName("test for del 1");
        testCat1.setCatalogId(1);
        CatalogDto testCat1Dto = catalogAction.saveCatalog(dtoMapper.mapCatalog(testCat1));
        Catalog testCat2 = dtoMapper.mapCatalog(mainCat);
        testCat2.setId(0);
        testCat2.setName("test for del 2");
        testCat2.setCatalogId(testCat1Dto.getId());
        catalogAction.saveCatalog(dtoMapper.mapCatalog(testCat2));
        Catalog testCat3 = dtoMapper.mapCatalog(mainCat);
        testCat3.setId(0);
        testCat3.setName("test for del 3");
        testCat3.setCatalogId(testCat1Dto.getId());
        catalogAction.saveCatalog(dtoMapper.mapCatalog(testCat3));

        testCat1Dto = catalogAction.loadCatalog(testCat1Dto.getId());
        testCat1Dto.getContentList().remove(0);
        testCat1Dto= catalogAction.saveCatalog(testCat1Dto);
        int actual = catalogAction.loadCatalog(testCat1Dto.getId()).getContentList().size();
        catalogAction.deleteCatalog(testCat1Dto);
        Assert.assertEquals(1,actual);
    }

}


