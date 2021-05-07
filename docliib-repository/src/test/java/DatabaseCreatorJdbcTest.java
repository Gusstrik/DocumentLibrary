import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.repository.CatalogDao;
import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;

import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DatabaseCreatorJdbcTest {

    private static final ApplicationContext appContext = new AnnotationConfigApplicationContext(RepositoryConfiguration.class);

    private static final DatabaseCreatorJdbc creator = appContext.getBean(DatabaseCreatorJdbc.class);

    private static final CatalogDao catalogDao = appContext.getBean("CatalogJpa",CatalogDao.class);

    @Test
    public void createDatabaseTest() {
        creator.createDatabse();
        Catalog homeCat = catalogDao.loadCatalog(1);
        Assert.assertEquals("/", homeCat.getName());
    }

    @Test
    public void runScriptTest(){
        creator.runScript("src/test/resources/insertestdb.sql");
        Catalog catalog = catalogDao.loadCatalog(1);
        List<Unit> list = catalog.getContentList();
        List<String> expected = new ArrayList<>();
        expected.add("test_catalog");
        expected.add("test_doc");
        List<String> actual = new ArrayList<>();
        for (Unit u:list){
            actual.add(u.getName());
        }
        creator.runScript("src/test/resources/deletedb.sql");
        Assert.assertEquals(expected,actual);
    }
}
