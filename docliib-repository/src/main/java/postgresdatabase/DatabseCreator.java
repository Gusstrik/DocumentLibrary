package postgresdatabase;


import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabseCreator {
    public void createDatabse()  {
        try {
            Connection connection = DatabaseConnector.getConnectionFromPool();
            ScriptRunner sr = new ScriptRunner(connection);
            Reader reader = new FileReader
                    ("C:\\Users\\istre\\DocumentLibrary\\docliib-repository\\src\\main\\resources\\createdb.sql");
            sr.runScript(reader);
        }catch (SQLException | FileNotFoundException e){
            e.printStackTrace();
        }
    }
}
