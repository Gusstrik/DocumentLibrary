package servicetest;

import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.dto.PermissionDto;
import com.strelnikov.doclib.model.roles.PermissionType;
import com.strelnikov.doclib.model.roles.Client;

import com.strelnikov.doclib.repository.ClientDao;
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
}
