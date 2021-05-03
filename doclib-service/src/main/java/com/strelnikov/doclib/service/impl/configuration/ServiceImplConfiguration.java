package com.strelnikov.doclib.service.impl.configuration;

import com.strelnikov.doclib.repository.CatalogDao;
import com.strelnikov.doclib.repository.DocumentDao;
import com.strelnikov.doclib.repository.FileDao;
import com.strelnikov.doclib.repository.TypeDao;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.strelnikov.doclib.service.impl")
public class ServiceImplConfiguration {

    private final ApplicationContext appContext = new AnnotationConfigApplicationContext(RepositoryConfiguration.class);

    private final CatalogDao catalogDao=appContext.getBean(CatalogDao.class);
    private final FileDao fileDao = appContext.getBean(FileDao.class);
    private final DocumentDao documentDao = appContext.getBean(DocumentDao.class);
    private final TypeDao typeDao = appContext.getBean(TypeDao.class);

    @Bean
    public CatalogDao catalogDao(){
        return catalogDao;
    }

    @Bean
    public FileDao fileDao(){
        return fileDao;
    }

    @Bean
    public DocumentDao documentDao(){
        return documentDao;
    }

    @Bean
    public TypeDao typeDao(){
        return typeDao;
    }
}
