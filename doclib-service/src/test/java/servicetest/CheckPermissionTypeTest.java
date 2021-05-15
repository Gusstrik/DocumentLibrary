package servicetest;

import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.model.roles.PermissionType;
import com.strelnikov.doclib.model.roles.Client;

import com.strelnikov.doclib.repository.ClientDao;
import com.strelnikov.doclib.service.CatalogActions;
import com.strelnikov.doclib.service.SecurityActions;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import com.strelnikov.doclib.service.impl.configuration.ServiceImplConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class CheckPermissionTypeTest {
    private ApplicationContext appContext = new AnnotationConfigApplicationContext(ServiceImplConfiguration.class);
    private SecurityActions securityActions = appContext.getBean(SecurityActions.class);
    private CatalogActions catalogActions = appContext.getBean(CatalogActions.class);
    private ClientDao clientDao = appContext.getBean(ClientDao.class);

    @Test
    public void checkPermissionTest() throws UnitNotFoundException {
        CatalogDto catalogDto = catalogActions.loadCatalog(1);
        Client client = clientDao.findBylogin("root");
        Assert.assertTrue(securityActions.checkPermission(1,catalogDto, "root", PermissionType.READING));
    }
}
