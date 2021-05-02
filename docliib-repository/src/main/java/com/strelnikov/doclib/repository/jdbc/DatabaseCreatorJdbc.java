package com.strelnikov.doclib.repository.jdbc;


import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@Repository
public class DatabaseCreatorJdbc {

    private DataSource dataSource;

    public DatabaseCreatorJdbc(@Autowired DataSource dataSource){
        this.dataSource = dataSource;
    }

    public void createDatabse() {
        try (Connection connection = dataSource.getConnection();
             Reader reader = new FileReader("src/main/resources/createdb.sql")){
            ScriptRunner sr = new ScriptRunner(connection);
            sr.runScript(reader);
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
            log.error(e.getMessage(), e);
        }
    }

    public void runScript(String sqriptSqlPath){
        try (Connection connection = dataSource.getConnection();
             Reader reader = new FileReader(sqriptSqlPath)){
            ScriptRunner sr = new ScriptRunner(connection);
            sr.runScript(reader);
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
            log.error(e.getMessage(), e);
        }
    }
}
