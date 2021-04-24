package com.strelnikov.doclib.postgresdatabase;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DatabaseConnector {
    private static BasicDataSource ds = new BasicDataSource();
    static{
        ResourceBundle resourse = ResourceBundle.getBundle("database");
        //String dbName = resourse.getString("db.name");
        ds.setUrl(resourse.getString("db.url"));
        ds.setUsername(resourse.getString("db.user"));
        ds.setPassword(resourse.getString("db.password"));
    }
    public static Connection getConnectionFromPool() throws SQLException {
        return ds.getConnection();
    }
}
