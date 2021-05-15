package servicetest;

import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.dto.PermissionDto;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.roles.PermissionType;
import com.strelnikov.doclib.model.roles.Client;

import com.strelnikov.doclib.repository.CatalogDao;
import com.strelnikov.doclib.repository.ClientDao;
import com.strelnikov.doclib.repository.PermissionDao;
import com.strelnikov.doclib.service.CatalogActions;
import com.strelnikov.doclib.service.SecurityActions;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import com.strelnikov.doclib.service.impl.configuration.ServiceImplConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class SecurityImplTest {
    private ApplicationContext appContext = new AnnotationConfigApplicationContext(ServiceImplConfiguration.class);
    private SecurityActions securityActions = appContext.getBean(SecurityActions.class);
    private CatalogActions catalogActions = appContext.getBean(CatalogActions.class);
    private ClientDao clientDao = appContext.getBean(ClientDao.class);
    private DtoMapper dtoMapper = appContext.getBean(DtoMapper.class);
    private CatalogDao catalogDao = appContext.getBean(CatalogDao.class);
    private PermissionDao permissionDao = appContext.getBean(PermissionDao.class);

    @Test
    public void checkPermissionTest() throws UnitNotFoundException {
        CatalogDto catalogDto = catalogActions.loadCatalog(1);
        Assert.assertTrue(securityActions.checkPermission(catalogDto, "root", PermissionType.READING));
    }

    @Test
    public void getPermissionTest() throws UnitNotFoundException {
        CatalogDto catalogDto = catalogActions.loadCatalog(1);
        int actual = securityActions.getObjectPermissions(catalogDto).size();
        Assert.assertEquals(1,actual);
    }

    @Test
    public void inheritPermissionTest() throws UnitNotFoundException {
        Catalog catalog = catalogDao.loadCatalog(1);
        catalog.setCatalogId(1);
        catalog.setId(0);
        catalog.setName("test_catalog");
        catalog = catalogDao.insertCatalog(catalog);
        permissionDao.addObjectToSecureTables(catalog);
        securityActions.inheritPermissions(dtoMapper.mapCatalog(catalog), catalogActions.loadCatalog(catalog.getCatalogId()));
        boolean actual = securityActions.checkPermission(dtoMapper.mapCatalog(catalog),"root",PermissionType.MODERATING);
        catalogDao.deleteCatalog(catalog.getId());
        permissionDao.removeObjectFromSecureTables(catalog);
        Assert.assertTrue(actual);
    }
}
