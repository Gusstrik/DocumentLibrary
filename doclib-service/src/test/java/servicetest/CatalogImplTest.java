package servicetest;

import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.service.dtomapper.configuration.DtoMapperConfiguration;
import com.strelnikov.doclib.service.exceptions.UnitIsAlreadyExistException;
import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import com.strelnikov.doclib.service.CatalogActions;
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
public class CatalogImplTest {

    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(ServiceImplConfiguration.class,
            RepositoryConfiguration.class, DtoMapperConfiguration.class);

    private static final DatabaseCreatorJdbc creator = appContext.getBean(DatabaseCreatorJdbc.class);
    private final CatalogActions catalogAction = appContext.getBean(CatalogActions.class);
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
    public void saveCatalogTest() throws UnitIsAlreadyExistException, UnitNotFoundException {
        CatalogDto mainCat = catalogAction.loadCatalog(1);
        int expected = mainCat.getContentList().size();
        Catalog catalog = new Catalog();
        catalog.setName("test catalog");
        catalog.setParent_id(1);
        CatalogDto catalogDto = catalogAction.saveCatalog(dtoMapper.mapCatalog(catalog));
        expected += 1;
        mainCat = catalogAction.loadCatalog(1);
        int actual = mainCat.getContentList().size();
        catalogAction.deleteCatalog(catalogDto);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void deleteCatalogTest() throws UnitNotFoundException, UnitIsAlreadyExistException {
        CatalogDto mainCat = catalogAction.loadCatalog(1);
        int expected = mainCat.getContentList().size();
        Catalog catalog = new Catalog();
        catalog.setName("test catalog");
        catalog.setParent_id(1);
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
    public void editCatalogTest() throws UnitIsAlreadyExistException, UnitNotFoundException {
        Catalog catalog = new Catalog();
        catalog.setName("test catalog");
        catalog.setParent_id(1);
        CatalogDto catalogDto = catalogAction.saveCatalog(dtoMapper.mapCatalog(catalog));
        catalog = new Catalog();
        catalog.setName("test for del 1");
        catalog.setParent_id(catalogDto.getId());
        catalogAction.saveCatalog(dtoMapper.mapCatalog(catalog));
        catalog = new Catalog();
        catalog.setName("test for del 2");
        catalog.setParent_id(catalogDto.getId());
        catalogAction.saveCatalog(dtoMapper.mapCatalog(catalog));
        catalogDto = catalogAction.loadCatalog(catalogDto.getId());
        catalogDto.getContentList().remove(0);
        catalogDto= catalogAction.saveCatalog(catalogDto);
        int actual = catalogAction.loadCatalog(catalogDto.getId()).getContentList().size();
        catalogAction.deleteCatalog(catalogDto);
        Assert.assertEquals(1,actual);
    }

}


