import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.repository.jdbc.CatalogDaoJdbc;
import com.strelnikov.doclib.repository.jdbc.DatabaseConnectorJdbc;
import com.strelnikov.doclib.repository.jdbc.DatabaseCreatorJdbc;

import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
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

    @Test
    public void createDatabaseTest() {
        creator.createDatabse();
        String homeCatName = "";
        log.info("Database was successfully created");
        try(Connection connection = DatabaseConnectorJdbc.getConnectionFromPool()) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select name from catalogs where name='/'");
            rs.next();
            homeCatName = rs.getString(1);
        } catch (SQLException e) {
            log.error(e.getMessage(), e.getSQLState());
        }
        Assert.assertEquals("/", homeCatName);
    }

    @Test
    public void runScriptTest(){
        creator.runScript("src/test/resources/insertestdb.sql");
        CatalogDaoJdbc catalogDaoJdbc = appContext.getBean(CatalogDaoJdbc.class);
        Catalog catalog = catalogDaoJdbc.loadCatalog("test_parent");
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
