package servicetest;

import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.roles.PermissionType;

import com.strelnikov.doclib.repository.CatalogDao;
import com.strelnikov.doclib.repository.ClientDao;
import com.strelnikov.doclib.repository.PermissionDao;
import com.strelnikov.doclib.service.CatalogService;
import com.strelnikov.doclib.service.SecurityService;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import com.strelnikov.doclib.service.impl.configuration.ServiceImplConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SecurityServiceImplTest {
    private ApplicationContext appContext = new AnnotationConfigApplicationContext(ServiceImplConfiguration.class);
    private SecurityService securityService = appContext.getBean(SecurityService.class);
    private CatalogService catalogService = appContext.getBean(CatalogService.class);
    private ClientDao clientDao = appContext.getBean(ClientDao.class);
    private DtoMapper dtoMapper = appContext.getBean(DtoMapper.class);
    private CatalogDao catalogDao = appContext.getBean(CatalogDao.class);
    private PermissionDao permissionDao = appContext.getBean(PermissionDao.class);

    @Test
    public void checkPermissionTest() throws UnitNotFoundException {
        CatalogDto catalogDto = catalogService.loadCatalog(1);
        Assert.assertTrue(securityService.checkPermission(catalogDto, "root", PermissionType.READING));
    }

    @Test
    public void getPermissionTest() throws UnitNotFoundException {
        CatalogDto catalogDto = catalogService.loadCatalog(1);
        int actual = securityService.getObjectPermissions(catalogDto).size();
        Assert.assertEquals(2,actual);
    }

    @Test
    public void inheritPermissionTest() throws UnitNotFoundException {
        Catalog catalog = catalogDao.loadCatalog(1);
        catalog.setCatalogId(1);
        catalog.setId(0);
        catalog.setName("test_catalog");
        catalog = catalogDao.insertCatalog(catalog);
        securityService.addObjectToSecureTable(catalog);
        securityService.inheritPermissions(catalog, catalogDao.loadCatalog(catalog.getCatalogId()));
        boolean actual = securityService.checkPermission(dtoMapper.mapCatalog(catalog),"root",PermissionType.MODERATING);
        catalogDao.deleteCatalog(catalog.getId());
        securityService.removeObjectFromSecureTable(catalog);
        Assert.assertTrue(actual);
    }
}
