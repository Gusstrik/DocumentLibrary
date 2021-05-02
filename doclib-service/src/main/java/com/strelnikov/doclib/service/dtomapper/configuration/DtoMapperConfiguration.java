package com.strelnikov.doclib.service.dtomapper.configuration;

import com.strelnikov.doclib.service.CatalogActions;
import com.strelnikov.doclib.service.DocumentActions;
import com.strelnikov.doclib.service.DocumentTypeActions;
import com.strelnikov.doclib.service.impl.configuration.ServiceImplConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan (basePackages = "com.strelnikov.doclib.service.dtomapper.impl")
public class DtoMapperConfiguration {

    private final ApplicationContext appContext = new AnnotationConfigApplicationContext(ServiceImplConfiguration.class);
    private final CatalogActions catalogActions = appContext.getBean(CatalogActions.class);
    private final DocumentActions documentActions = appContext.getBean(DocumentActions.class);
    private final DocumentTypeActions documentTypeActions = appContext.getBean(DocumentTypeActions.class);

    @Bean
    public CatalogActions catalogActions(){
        return catalogActions;
    }

    @Bean
    public DocumentTypeActions documentTypeActions(){
        return documentTypeActions;
    }

    @Bean
    public DocumentActions documentActions(){
        return documentActions;
    }
}
