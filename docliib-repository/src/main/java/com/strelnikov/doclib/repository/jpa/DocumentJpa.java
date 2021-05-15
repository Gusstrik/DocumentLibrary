package com.strelnikov.doclib.repository.jpa;

import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentType;
import com.strelnikov.doclib.model.documnets.DocumentVersion;
import com.strelnikov.doclib.repository.DocVersionDao;
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
import java.util.List;

@Repository("DocumentJpa")
public class DocumentJpa implements DocumentDao {

    @Autowired
    @Qualifier("DocVersionJpa")
    private DocVersionDao docVersionDao;

    private EntityManager getEntityManager(){
        ApplicationContext appContext = new AnnotationConfigApplicationContext(RepositoryConfiguration.class);
        EntityManagerFactory entityManagerFactory = appContext.getBean(EntityManagerFactory.class);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager;
    }

    @Override
    public Document insertDocument(Document document) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(document);
        em.getTransaction().commit();
        em.close();
        return document;
    }

    @Override
    public Document loadDocument(int docId) {
        EntityManager entityManager = getEntityManager();
        Document doc = entityManager.find(Document.class,docId);
        entityManager.close();
        if(doc!=null) {
            doc.setVersionsList(docVersionDao.getDocVersionList(doc.getId()));
        }
        return doc;
    }

    @Override
    public void updateDocument(Document document) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.merge(document);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public List<Unit> getDocumentsList(int catalogId) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT doc FROM Document doc WHERE doc.catalogId="+catalogId);
        List<Unit> result = query.getResultList();
        em.close();
        return result;
    }

    @Override
    public void deleteDocument(int documentId) {
        EntityManager em=getEntityManager();
        Document doc = em.find(Document.class,documentId);
        em.getTransaction().begin();
        em.remove(doc);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Document findByName(String name){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT doc FROM Document doc WHERE doc.name=:name");
        query.setParameter("name",name);
        Document document = (Document)query.getSingleResult();
        return loadDocument(document.getId());
    }
}
