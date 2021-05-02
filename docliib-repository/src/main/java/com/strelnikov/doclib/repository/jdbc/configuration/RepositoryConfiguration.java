package com.strelnikov.doclib.repository.jdbc.configuration;

import com.strelnikov.doclib.repository.jdbc.*;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.ResourceBundle;

@Configuration
public class RepositoryConfiguration {

    private static BasicDataSource dataSource = new BasicDataSource();

    static {
        ResourceBundle resourse = ResourceBundle.getBundle("database");
        dataSource.setUrl(resourse.getString("db.url"));
        dataSource.setUsername(resourse.getString("db.user"));
        dataSource.setPassword(resourse.getString("db.password"));
    }


    @Bean
    public CatalogDaoJdbc catalogDaoJdbc() {
        return new CatalogDaoJdbc(dataSource);
    }

    @Bean
    public DatabaseCreatorJdbc databaseCreatorJdbc(){
        return new DatabaseCreatorJdbc(dataSource);
    }

    @Bean
    public DocumentDaoJdbc documentDaoJdbc(){
        return new DocumentDaoJdbc(dataSource);
    }

    @Bean
    public FileDaoJdbc fileDaoJdbc(){
        return new FileDaoJdbc(dataSource);
    }

    @Bean
    public TypeDaoJdbc typeDaoJdbc(){
        return new TypeDaoJdbc(dataSource);
    }
}
