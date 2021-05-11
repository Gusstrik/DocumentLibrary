package com.strelnikov.doclib.repository.jpa;

import com.strelnikov.doclib.model.documnets.DocumentFile;
import com.strelnikov.doclib.model.documnets.DocumentVersion;
import com.strelnikov.doclib.repository.DocFileDao;
import com.strelnikov.doclib.repository.configuration.RepositoryConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

@Repository(value = "DocFileJpa")
public class DocFileJpa implements DocFileDao {

    private EntityManager getEntityManager(){
        ApplicationContext appContext = new AnnotationConfigApplicationContext(RepositoryConfiguration.class);
        EntityManagerFactory entityManagerFactory = appContext.getBean(EntityManagerFactory.class);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager;
    }

    @Override
    public DocumentFile insertFile(DocumentFile file) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(file);
        em.getTransaction().commit();
        em.close();
        return file;
    }

    @Override
    public void deleteFile(int fileId) {
        EntityManager em=getEntityManager();
        DocumentFile docFile = em.find(DocumentFile.class,fileId);
        em.getTransaction().begin();
        em.remove(docFile);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public List<DocumentFile> getFilesList(DocumentVersion documentVersion) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT docFile FROM DocumentFile docFile WHERE docFile.docVersion.id=:id");
        query.setParameter("id",documentVersion.getId());
        List<DocumentFile> result = query.getResultList();
        em.close();
        return result;
    }
}
