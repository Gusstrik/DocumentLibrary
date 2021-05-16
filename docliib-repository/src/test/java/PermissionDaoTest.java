import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.roles.Client;
import com.strelnikov.doclib.model.roles.PermissionType;
import com.strelnikov.doclib.repository.CatalogDao;
import com.strelnikov.doclib.repository.ClientDao;
import com.strelnikov.doclib.repository.PermissionDao;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class PermissionDaoTest {

    private ApplicationContext appContext = new AnnotationConfigApplicationContext(RepositoryConfiguration.class);
    private PermissionDao checkPermission = appContext.getBean(PermissionDao.class);
    private ClientDao clientDao = appContext.getBean(ClientDao.class);
    private CatalogDao catalogDao = appContext.getBean(CatalogDao.class);
    private  Catalog catalog = catalogDao.loadCatalog(1);

    @Test
    public void getClassIdTest(){
        int actual = checkPermission.getClassId(Catalog.class);
        Assert.assertEquals(1,actual);
    }

    @Test
    public void getObjectIdTest(){
        int actual = checkPermission.getObjectSecureId(catalog);
        Assert.assertEquals(1,actual);
    }

    @Test
    public void checkPermissionTest(){
        boolean actual = true;
        actual = actual && checkPermission.checkPermission(catalog,clientDao.findBylogin("root"), PermissionType.MODERATING);
        actual = actual && checkPermission.checkPermission(catalog,clientDao.findBylogin("root"), PermissionType.WRITING);
        actual = actual && checkPermission.checkPermission(catalog,clientDao.findBylogin("root"), PermissionType.READING);
        Assert.assertTrue(actual);
    }

    @Test
    public void getClientPermissionTest(){
        int actual = checkPermission.getClientPermissions(clientDao.findBylogin("root")).size();
        Assert.assertEquals(1,actual);
    }

    @Test
    public void getObjPermissionTest(){
        int actual = checkPermission.getPermissionsOfSecuredObject(catalogDao.loadCatalog(1)).size();
        Assert.assertEquals(2,actual);
    }

    @Test
    public void updatePermissionTest(){
        checkPermission.updatePermission(catalogDao.loadCatalog(1),clientDao.findBylogin("root"),0);
        boolean actual = checkPermission.checkPermission(catalogDao.loadCatalog(1),clientDao.findBylogin("root"),PermissionType.READING);
        checkPermission.updatePermission(catalogDao.loadCatalog(1),clientDao.findBylogin("root"),7);
        Assert.assertFalse(actual);
    }

    @Test
    public void addClientTest(){
        Client client = clientDao.findBylogin("root");
        client.setId(0);
        client.setLogin("test");
        client = clientDao.create(client);
        checkPermission.addClientToSecureTables(client);
        checkPermission.updatePermission(catalogDao.loadCatalog(1),client,7);
        boolean actual = checkPermission.checkPermission(catalogDao.loadCatalog(1),client,PermissionType.READING);
        clientDao.delete(client);
        Assert.assertTrue(actual);
    }

    @Test
    public void addObjectTest(){
        Catalog catalog = catalogDao.loadCatalog(1);
        catalog.setId(0);
        catalog.setCatalogId(1);
        catalog.setName("test_catalog");
        catalog = catalogDao.insertCatalog(catalog);
        checkPermission.addObjectToSecureTables(catalog);
        checkPermission.updatePermission(catalog,clientDao.findBylogin("root"),2);
        boolean actual = checkPermission.checkPermission(catalog,clientDao.findBylogin("root"),PermissionType.WRITING);
        catalogDao.deleteCatalog(catalog.getId());
        checkPermission.removeObjectFromSecureTables(catalog);
        Assert.assertTrue(actual);
    }
}
