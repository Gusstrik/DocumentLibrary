package com.strelnikov.doclib.repository.jpa;

import com.strelnikov.doclib.model.roles.Authority;
import com.strelnikov.doclib.model.roles.Client;
import com.strelnikov.doclib.repository.ClientDao;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.HashSet;
import java.util.List;

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

    @Override
    public Client create(Client client) {
        EntityManager em = getEntityManager();
        Client newClient = new Client();
        newClient.setLogin(client.getLogin());
        newClient.setPassword(client.getPassword());
        newClient.setAuthorities(new HashSet<>());
        List<Authority> authorityList = client.getAuthorities().stream().toList();
        for (Authority authority: authorityList){
            newClient.getAuthorities().add(em.find(Authority.class,authority.getId()));
        }
        em.getTransaction().begin();
        em.persist(newClient);
        em.getTransaction().commit();
        em.close();
        return newClient;
    }

    @Override
    public void delete(Client client) {
        EntityManager em = getEntityManager();
        client = em.find(Client.class,client.getId());
        em.getTransaction().begin();
        em.remove(client);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Client findById(int id) {
       EntityManager em = getEntityManager();
       Client client = em.find(Client.class,id);
       return client;
    }
}
