import com.strelnikov.doclib.database.jdbc.CatalogDaoJdbc;
import com.strelnikov.doclib.database.jdbc.DatabaseConnectorJdbc;
import com.strelnikov.doclib.database.jdbc.DatabaseCreatorJdbc;

import com.strelnikov.doclib.model.conception.Unit;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DatabaseCreatorJdbcTest {

    @Test
    public void createDatabaseTest() {
        DatabaseCreatorJdbc creator = new DatabaseCreatorJdbc();
        creator.createDatabse();
        String homeCatName = "";
        log.info("Database was successfully created");
        try(Connection connection = DatabaseConnectorJdbc.getConnectionFromPool()) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select name from catalog where name='/'");
            rs.next();
            homeCatName = rs.getString(1);
        } catch (SQLException e) {
            log.error(e.getMessage(), e.getSQLState());
        }
        Assert.assertEquals("/", homeCatName);
    }

    @Test
    public void runScriptTest(){
        DatabaseCreatorJdbc creator = new DatabaseCreatorJdbc();
        creator.runScript("src/test/resources/insertestdb.sql");
        CatalogDaoJdbc catalogDaoJdbc = new CatalogDaoJdbc();
        List<Unit> list = catalogDaoJdbc.getContentList("test_parent");
        List<String> expected = new ArrayList();
        expected.add("test_catalog");
        expected.add("test_doc");
        List<String> actual = new ArrayList();
        for (Unit u:list){
            actual.add(u.getName());
        }
        creator.runScript("src/test/resources/deletedb.sql");
        Assert.assertEquals(expected,actual);
    }
}
