package com.strelnikov.doclib.web.config;

import com.strelnikov.doclib.service.impl.configuration.ServiceImplConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


import javax.servlet.ServletContext;

@Configuration
@Import({ServiceImplConfiguration.class,SwaggerConfig.class})
@ComponentScan(basePackages = "com.strelnikov.doclib.web.controllers")
public class MvcConfig {

}
