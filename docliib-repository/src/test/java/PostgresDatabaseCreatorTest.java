import com.strelnikov.doclib.postgresdatabase.DatabaseConnector;
import com.strelnikov.doclib.postgresdatabase.DatabaseCreator;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgresDatabaseCreatorTest {
    private static final Logger log;

    static {
        log = LoggerFactory.getLogger("PostgresTest");
    }

    @Test
    public void createDatabaseTest() {
        DatabaseCreator creator = new DatabaseCreator();
        creator.createDatabse();
        String homeCatName = "";
        log.info("Database was successfully created");
        try {
            Connection connection = DatabaseConnector.getConnectionFromPool();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select name from catalog where name='/'");
            rs.next();
            homeCatName = rs.getString(1);
        } catch (SQLException e) {
            log.error(e.getMessage(), e.getSQLState());
        }
        Assert.assertEquals("/", homeCatName);
    }
}
