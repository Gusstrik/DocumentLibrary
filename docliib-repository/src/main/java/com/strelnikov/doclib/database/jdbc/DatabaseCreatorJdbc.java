package com.strelnikov.doclib.database.jdbc;


import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseCreatorJdbc {
    private static final Logger log;
    static {
        log = LoggerFactory.getLogger(DatabaseCreatorJdbc.class);
    }
    public void createDatabse()  {
        try {
            Connection connection = DatabaseConnectorJdbc.getConnectionFromPool();
            ScriptRunner sr = new ScriptRunner(connection);
            Reader reader = new FileReader
                    ("src/main/resources/createdb.sql");
            sr.runScript(reader);
        }catch (SQLException | FileNotFoundException e){
            System.out.println(e.getMessage());
            log.error(e.getMessage(),e);
        }
    }
}
