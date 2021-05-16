package com.strelnikov.doclib.springboot.app;

import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import com.strelnikov.doclib.service.dtomapper.configuration.DtoMapperConfiguration;
import com.strelnikov.doclib.service.impl.configuration.ServiceImplConfiguration;
import com.strelnikov.doclib.web.config.MvcConfig;
import com.strelnikov.doclib.web.security.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@Import({SecurityConfig.class, MvcConfig.class})
@SpringBootApplication
public class DoclibApp {
    public static void main(String[] args) {
        try {
            SpringApplication.run(DoclibApp.class, args);
        }catch (Exception e ){
            e.printStackTrace();
        }
    }
}
