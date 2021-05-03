package com.strelnikov.doclib.service.impl.configuration;

import com.strelnikov.doclib.repository.CatalogDao;
import com.strelnikov.doclib.repository.DocFileDao;
import com.strelnikov.doclib.repository.DocTypeDao;
import com.strelnikov.doclib.repository.DocumentDao;
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
    private final DocFileDao docFileDao = appContext.getBean(DocFileDao.class);
    private final DocumentDao documentDao = appContext.getBean(DocumentDao.class);
    private final DocTypeDao docTypeDao = appContext.getBean(DocTypeDao.class);

    @Bean
    public CatalogDao catalogDao(){
        return catalogDao;
    }

    @Bean
    public DocFileDao fileDao(){
        return docFileDao;
    }

    @Bean
    public DocumentDao documentDao(){
        return documentDao;
    }

    @Bean
    public DocTypeDao typeDao(){
        return docTypeDao;
    }
}
