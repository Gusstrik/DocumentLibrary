import com.strelnikov.doclib.model.roles.Client;
import com.strelnikov.doclib.repository.ClientDao;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ClientDaoTest {
    private ApplicationContext appContext = new AnnotationConfigApplicationContext(RepositoryConfiguration.class);
    private ClientDao clientDao = appContext.getBean(ClientDao.class);

    @Test
    public void getByLoginTest(){
        Client client = clientDao.findBylogin("root");
        Assert.assertEquals(1,client.getId().intValue());
    }

    @Test
    public void getByIdTest(){
        Client client = clientDao.findById(1);
        Assert.assertEquals("root",client.getLogin());
    }
}
