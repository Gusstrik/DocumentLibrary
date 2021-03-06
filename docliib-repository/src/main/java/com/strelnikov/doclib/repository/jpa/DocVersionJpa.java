package com.strelnikov.doclib.repository.jpa;

import com.strelnikov.doclib.model.documnets.DocumentFile;
import com.strelnikov.doclib.model.documnets.DocumentVersion;
import com.strelnikov.doclib.repository.DocFileDao;
import com.strelnikov.doclib.repository.DocVersionDao;
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

@Repository("DocVersionJpa")
public class DocVersionJpa implements DocVersionDao {

    @Autowired
    @Qualifier("DocFileJpa")
    private DocFileDao docFileDao;

    private EntityManager getEntityManager(){
        ApplicationContext appContext = new AnnotationConfigApplicationContext(RepositoryConfiguration.class);
        EntityManagerFactory entityManagerFactory = appContext.getBean(EntityManagerFactory.class);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager;
    }

    @Override
    public DocumentVersion insertDocVersion(DocumentVersion documentVersion) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(documentVersion);
        em.getTransaction().commit();
        em.close();
        return documentVersion;
    }

    @Override
    public void deleteDocVersion(int versionId) {
        EntityManager em=getEntityManager();
        DocumentVersion docVer = em.find(DocumentVersion.class,versionId);
        em.getTransaction().begin();
        em.remove(docVer);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public List<DocumentVersion> getDocVersionList(int docId) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT docVer FROM DocumentVersion docVer WHERE docVer.parentDocument.id="+ docId);
        List<DocumentVersion> result = query.getResultList();
        for(DocumentVersion docVer:result){
            docVer.setFilesList(docFileDao.getFilesList(docVer));
        }
        em.close();
        return result;
    }

    @Override
    public DocumentVersion loadDocVersion(int docVerId) {
        EntityManager em = getEntityManager();
        DocumentVersion docVer = em.find(DocumentVersion.class,docVerId);
        docVer.setFilesList(docFileDao.getFilesList(docVer));
        em.close();
        return docVer;
    }

    @Override
    public List<DocumentFile> getFileList(DocumentVersion docVersion) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        List<DocumentFile> fileList = docVersion.getFilesList();
        em.getTransaction().commit();
        em.close();
        return fileList;
    }

    @Override
    public DocumentVersion updateVersion(DocumentVersion documentVersion) {
       EntityManager em = getEntityManager();
       em.getTransaction().begin();
       em.merge(documentVersion);
       em.getTransaction().commit();
       em.close();
       return documentVersion;
    }


}
