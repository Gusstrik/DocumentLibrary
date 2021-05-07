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
    public void insertType(String type) {
        DocumentType docType = new DocumentType(type);
        EntityManager entityManager = getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(docType);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void deleteType(String type) {
        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery("SELECT docType FROM DocumentType docType " +
                "WHERE docType.curentType='"+ type+"'");

        DocumentType documentType = (DocumentType)query.getSingleResult();
        entityManager.getTransaction().begin();
        entityManager.remove(documentType);
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
    public List<String> getTypesList() {
        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createQuery("SELECT docType FROM DocumentType docType");
        List<DocumentType> docTypeList= query.getResultList();
        List<String> result = new ArrayList<>();
        for(DocumentType docType:docTypeList){
           result.add(docType.getCurentType());
        }
        entityManager.close();
        return result;
    }
}
