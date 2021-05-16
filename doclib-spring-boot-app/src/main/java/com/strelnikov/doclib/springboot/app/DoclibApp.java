package com.strelnikov.doclib.springboot.app;

import com.strelnikov.doclib.web.config.MvcConfig;
import com.strelnikov.doclib.web.security.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
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
