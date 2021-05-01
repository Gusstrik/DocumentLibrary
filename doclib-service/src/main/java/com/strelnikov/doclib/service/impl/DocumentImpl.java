package com.strelnikov.doclib.service.impl;

import com.strelnikov.doclib.repository.CatalogDao;
import com.strelnikov.doclib.repository.DocumentDao;
import com.strelnikov.doclib.repository.FileDao;
import com.strelnikov.doclib.repository.jdbc.CatalogDaoJdbc;
import com.strelnikov.doclib.repository.jdbc.DocumentDaoJdbc;
import com.strelnikov.doclib.repository.jdbc.FileDaoJdbc;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentType;
import com.strelnikov.doclib.model.documnets.DocumentVersion;
import com.strelnikov.doclib.service.DocumentActions;

public class DocumentImpl implements DocumentActions {

    private final DocumentDao documentDao = new DocumentDaoJdbc();

    @Override
    public Document createNewDocument(String name, DocumentType docType, Catalog catalog) {
        Document document = new Document(name);
        document.setActualVersion(1);
        document.setDocumentType(docType);
        document.getVersionsList().add(new DocumentVersion());
        CatalogDaoJdbc catalogDaoJdbc = new CatalogDaoJdbc();
        documentDao.addNewDocument(document,catalogDaoJdbc.getCatalogId(catalog.getName()));
        return document;
    }

    @Override
    public void deleteDocumentVersion(Document document) {
        documentDao.deleteDocument(documentDao.getDocumentId(document));
        document.versionsList.remove(document.getActualVersion()-1);
        document.setActualVersion(document.getActualVersion()-1);
    }

    @Override
    public void createNewDocumentVersion(Document document, Catalog curentCatalog) {
        CatalogDao catalogDao = new CatalogDaoJdbc();
        documentDao.addNewDocument(document,catalogDao.getCatalogId(curentCatalog.getName()));
        FileDao fileDao = new FileDaoJdbc();
        DocumentVersion newVersion = document.getDocumentVersion();
        fileDao.copyFilesToNewDoc(newVersion.getFilesList(),documentDao.getDocumentId(document));
    }

    @Override
    public Document deleteNotActualVersions(Document document) {
        for (int i = 0; i< document.getActualVersion(); i++){
            Document tempDocVer = new Document(document.getName());
            tempDocVer.setDocumentType(document.getDocumentType());
            tempDocVer.setActualVersion(i);
            documentDao.deleteDocument(documentDao.getDocumentId(tempDocVer));
            document.getVersionsList().remove(i);
        }
        document.setActualVersion(1);
        return document;
    }

    @Override
    public void deleteDocument(Document document) {
        for (int i = 0; i <= document.getActualVersion(); i++){
            Document tempDocVer = new Document(document.getName());
            tempDocVer.setDocumentType(document.getDocumentType());
            tempDocVer.setActualVersion(i);
            documentDao.deleteDocument(documentDao.getDocumentId(tempDocVer));
        }
    }

    @Override
    public void refreshDocumentsFileList(Document document) {
        int documentId= documentDao.getDocumentId(document);
        DocumentVersion actualVersion = document.getDocumentVersion();
        FileDaoJdbc fileDaoJdbc = new FileDaoJdbc();
        actualVersion.setFilesList(fileDaoJdbc.getFilesList(documentId));
    }

    @Override
    public Document loadDocument(String name, DocumentType docType) {
        Document document = documentDao.loadDocument(name,docType.getCurentType());
        return document;
    }
}
