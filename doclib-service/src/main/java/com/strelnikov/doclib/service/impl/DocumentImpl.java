package com.strelnikov.doclib.service.impl;

import com.strelnikov.doclib.repository.CatalogDao;
import com.strelnikov.doclib.repository.DocumentDao;
import com.strelnikov.doclib.repository.FileDao;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentType;
import com.strelnikov.doclib.model.documnets.DocumentVersion;
import com.strelnikov.doclib.service.DocumentActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentImpl implements DocumentActions {

    private final DocumentDao documentDao;
    private final CatalogDao catalogDao;
    private final FileDao fileDao;

    public DocumentImpl(@Autowired DocumentDao documentDao, @Autowired CatalogDao catalogDao, @Autowired FileDao fileDao){
        this.documentDao =documentDao;
        this.catalogDao= catalogDao;
        this.fileDao = fileDao;
    }

    @Override
    public Document createNewDocument(String name, DocumentType docType, Catalog catalog) {
        Document document = new Document(name);
        document.setActualVersion(0);
        document.setDocumentType(docType);
        document.getVersionsList().add(new DocumentVersion());
        documentDao.addNewDocument(document,catalogDao.getCatalogId(catalog.getName()));
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
        documentDao.addNewDocument(document,catalogDao.getCatalogId(curentCatalog.getName()));
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
        actualVersion.setFilesList(fileDao.getFilesList(documentId));
    }

    @Override
    public Document loadDocument(String name, DocumentType docType) {
        return documentDao.loadDocument(name,docType.getCurentType());
    }
}
