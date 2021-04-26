import com.strelnikov.doclib.database.jdbc.DatabaseConnectorJdbc;
import com.strelnikov.doclib.database.jdbc.DatabaseCreatorJdbc;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgresDatabaseCreatorJdbcTest {
    private static final Logger log;

    static {
        log = LoggerFactory.getLogger("PostgresTest");
    }

    @Test
    public void createDatabaseTest() {
        DatabaseCreatorJdbc creator = new DatabaseCreatorJdbc();
        creator.createDatabse();
        String homeCatName = "";
        log.info("Database was successfully created");
        try {
            Connection connection = DatabaseConnectorJdbc.getConnectionFromPool();
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
