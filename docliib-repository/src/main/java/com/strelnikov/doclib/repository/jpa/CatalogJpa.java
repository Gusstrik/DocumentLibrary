package com.strelnikov.doclib.repository.jpa;

import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.repository.CatalogDao;
import com.strelnikov.doclib.repository.DocumentDao;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository("CatalogJpa")
public class CatalogJpa implements CatalogDao {

    @Autowired
    @Qualifier("DocumentJpa")
    DocumentDao documentDao;

    private EntityManager getEntityManager(){
        ApplicationContext appContext = new AnnotationConfigApplicationContext(RepositoryConfiguration.class);
        EntityManagerFactory entityManagerFactory = appContext.getBean(EntityManagerFactory.class);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager;
    }

    @Override
    public void updateCatalog(Catalog catalog) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.merge(catalog);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Catalog loadCatalog(int catalogId) {
        EntityManager entityManager = getEntityManager();
        Catalog cat = entityManager.find(Catalog.class,catalogId);
        entityManager.close();
        cat.setContentList(new ArrayList<>());
        cat.getContentList().addAll(getCatalogList(catalogId));
        cat.getContentList().addAll(documentDao.getDocumentsList(catalogId));
        return cat;
    }

    @Override
    public void deleteCatalog(int catalogId) {
        EntityManager em=getEntityManager();
        Catalog cat = em.find(Catalog.class,catalogId);
        em.getTransaction().begin();
        em.remove(cat);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Catalog insertCatalog(Catalog catalog) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(catalog);
        em.getTransaction().commit();
        em.close();
        return catalog;
    }

    private List<Unit> getCatalogList(int catalogId) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT cat FROM Catalog cat WHERE cat.catalogId="+catalogId);
        List<Unit> result = query.getResultList();
        em.close();
        return result;
    }
}
