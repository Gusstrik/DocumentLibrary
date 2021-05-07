package com.strelnikov.doclib.service.impl.configuration;

import com.strelnikov.doclib.repository.CatalogDao;
import com.strelnikov.doclib.repository.DocFileDao;
import com.strelnikov.doclib.repository.DocTypeDao;
import com.strelnikov.doclib.repository.DocumentDao;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import com.strelnikov.doclib.service.dtomapper.configuration.DtoMapperConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

@Configuration
@Import(DtoMapperConfiguration.class)
@ComponentScan(basePackages = "com.strelnikov.doclib.service.impl")
public class ServiceImplConfiguration {

}
