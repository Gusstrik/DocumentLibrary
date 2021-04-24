package com.strelnikov.doclib.postgresdatabase;


import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseCreator {
    private static final Logger log;
    static {
        log = LoggerFactory.getLogger(DatabaseCreator.class);
    }
    public void createDatabse()  {
        System.out.println(DatabaseCreator.class.getPackageName());
        try {
            Connection connection = DatabaseConnector.getConnectionFromPool();
            ScriptRunner sr = new ScriptRunner(connection);
            Reader reader = new FileReader
                    ("src/main/resources/createdb.sql");
            sr.runScript(reader);
        }catch (SQLException | FileNotFoundException e){
            log.error(e.getMessage(),e);
        }
    }
}
