import postgresdatabase.DatabaseConnector;
import postgresdatabase.DatabseCreator;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Test {
    public static void main(String[] args) {
        DatabseCreator creator = new DatabseCreator();
        creator.createDatabse();
    }
}
