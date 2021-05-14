package com.strelnikov.doclib.repository.jpa;

import com.strelnikov.doclib.model.roles.Client;
import com.strelnikov.doclib.repository.ClientDao;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

@Repository
public class ClientJpa implements ClientDao {

    private EntityManager getEntityManager(){

        ApplicationContext appContext = new AnnotationConfigApplicationContext(RepositoryConfiguration.class);
        EntityManagerFactory entityManagerFactory = appContext.getBean(EntityManagerFactory.class);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager;
    }

    @Override
    public Client findBylogin(String login) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT client FROM Client client WHERE client.login=:login");
        query.setParameter("login",login);
        Client client = (Client) query.getSingleResult();
        em.close();
        return client;
    }
}
