package com.strelnikov.doclib.web.servlets;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHolder implements ApplicationContextAware {
    private static ApplicationContext context;

    public static ApplicationContext getApplicationContext(){
        return context;
    }

    @Override
    public void setApplicationContext (ApplicationContext ap) throws BeansException {
        context=ap;
    }
}
