package com.strelnikov.doclib.repository.jpa;

import com.strelnikov.doclib.model.documnets.DocumentType;
import com.strelnikov.doclib.repository.DocTypeDao;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository(value="DocTypeJpa")
public class DocTypeJpa implements DocTypeDao {

    private EntityManager getEntityManager(){
        ApplicationContext appContext = new AnnotationConfigApplicationContext(RepositoryConfiguration.class);
        EntityManagerFactory entityManagerFactory = appContext.getBean(EntityManagerFactory.class);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager;
    }

    @Override
    public DocumentType insertType(DocumentType docType) {
        EntityManager entityManager = getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(docType);
        entityManager.getTransaction().commit();
        entityManager.close();
        return docType;
    }

    @Override
    public void deleteType(int typeId) {
        EntityManager entityManager = getEntityManager();
        DocumentType docType = entityManager.find(DocumentType.class,typeId);
        entityManager.getTransaction().begin();
        entityManager.remove(docType);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public DocumentType loadType(int typeId) {
        EntityManager entityManager = getEntityManager();
        DocumentType docType = entityManager.find(DocumentType.class,typeId);
        entityManager.close();
        return docType;
    }

    @Override
    public List<DocumentType> getTypesList() {
        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery("SELECT docType FROM DocumentType docType");
        List<DocumentType> docTypeList= query.getResultList();
        List<String> result = new ArrayList<>();
        entityManager.close();
        return docTypeList;
    }
}
