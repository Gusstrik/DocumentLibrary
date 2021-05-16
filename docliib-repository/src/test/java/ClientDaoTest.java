import com.strelnikov.doclib.model.roles.Authority;
import com.strelnikov.doclib.model.roles.AuthorityType;
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

    @Test
    public void addClientTest(){
        Client client = new Client();
        client.setLogin("test_user");
        client.setPassword("1234");
        Authority authority = new Authority();
        authority.setId(1);
        authority.setName(AuthorityType.ROLE_USER);
        client.getAuthorities().add(authority);
        client = clientDao.create(client);
        String actual = clientDao.findById(client.getId()).getLogin();
        clientDao.delete(client);
        Assert.assertEquals("test_user",actual);
    }

    @Test
    public void deleteClientTest(){
        Client client = new Client();
        client.setLogin("test_user");
        client.setPassword("1234");
        Authority authority = new Authority();
        authority.setId(1);
        authority.setName(AuthorityType.ROLE_USER);
        client.getAuthorities().add(authority);
        client = clientDao.create(client);
        clientDao.delete(client);
        Client actual = clientDao.findById(client.getId());
        Assert.assertNull(actual);
    }

    @Test
    public void updateClientTest(){
        Client client = new Client();
        client.setLogin("test_user");
        client.setPassword("1234");
        Authority authority = new Authority();
        authority.setId(1);
        authority.setName(AuthorityType.ROLE_USER);
        client.getAuthorities().add(authority);
        client = clientDao.create(client);
        client.setLogin("changed_login");
        client = clientDao.updateClient(client);
        String actual = clientDao.findById(client.getId()).getLogin();
        clientDao.delete(client);
        Assert.assertEquals("changed_login",actual);
    }
}
