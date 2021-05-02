package com.strelnikov.doclib.repository.jdbc.configuration;


import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


import javax.sql.DataSource;
import java.util.ResourceBundle;

@Configuration
@ComponentScan(basePackages = "com.strelnikov.doclib.repository")
public class RepositoryConfiguration {

    private static final BasicDataSource dataSource = new BasicDataSource();

    static {
        ResourceBundle resourse = ResourceBundle.getBundle("database");
        dataSource.setUrl(resourse.getString("db.url"));
        dataSource.setUsername(resourse.getString("db.user"));
        dataSource.setPassword(resourse.getString("db.password"));
    }

    @Bean
    public DataSource dataSource() {
        return dataSource;
    }
}
