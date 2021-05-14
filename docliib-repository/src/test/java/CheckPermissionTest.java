import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.conception.Permission;
import com.strelnikov.doclib.repository.ClientDao;
import com.strelnikov.doclib.repository.ICheckPermission;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class CheckPermissionTest {

    private ApplicationContext appContext = new AnnotationConfigApplicationContext(RepositoryConfiguration.class);
    private ICheckPermission checkPermission = appContext.getBean(ICheckPermission.class);
    private ClientDao clientDao = appContext.getBean(ClientDao.class);

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
        actual = actual && checkPermission.checkPermission(1,Catalog.class,clientDao.findBylogin("root"), Permission.MODERATING);
        actual = actual && checkPermission.checkPermission(1,Catalog.class,clientDao.findBylogin("root"), Permission.WRITING);
        actual = actual && checkPermission.checkPermission(1,Catalog.class,clientDao.findBylogin("root"), Permission.READING);
        Assert.assertTrue(actual);
    }
}
