import com.strelnikov.doclib.model.catalogs.Catalog;
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

    @Test
    public void getClassIdTest(){
        int actual = checkPermission.getClassId(Catalog.class);
        Assert.assertEquals(1,actual);
    }

    @Test
    public void getObjectIdTest(){
        int actual = checkPermission.getSecuredObjectId(1,Catalog.class);
        Assert.assertEquals(1,actual);
    }

    @Test
    public void checkPermissionTest(){
        boolean actual = true;
        actual = actual && checkPermission.checkPermission(1,Catalog.class,clientDao.findBylogin("root"), PermissionType.MODERATING);
        actual = actual && checkPermission.checkPermission(1,Catalog.class,clientDao.findBylogin("root"), PermissionType.WRITING);
        actual = actual && checkPermission.checkPermission(1,Catalog.class,clientDao.findBylogin("root"), PermissionType.READING);
        Assert.assertTrue(actual);
    }

    @Test
    public void getClientPermissionTest(){
        int actual = checkPermission.getClientPermissions(clientDao.findBylogin("root")).size();
        Assert.assertEquals(1,actual);
    }

    @Test
    public void getObjPermissionTest(){
        int actual = checkPermission.getPermissionsByObj(catalogDao.loadCatalog(1)).size();
        Assert.assertEquals(1,actual);
    }
}
