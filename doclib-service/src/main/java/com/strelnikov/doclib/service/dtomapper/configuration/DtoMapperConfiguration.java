package com.strelnikov.doclib.service.dtomapper.configuration;

import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan (basePackages = "com.strelnikov.doclib.service.dtomapper.impl")
@Import(RepositoryConfiguration.class)
public class DtoMapperConfiguration {

}
