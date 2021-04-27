package com.strelnikov.doclib.service;

import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentType;

public interface DocumentActions {
    Document createNewDocument(String name, DocumentType docType, Catalog catalog);

    void deleteDocumentVersion (Document document);

    void createNewDocumentVersion(Document document, Catalog catalog);

    Document deleteNotActualVersions (Document document);

    void deleteDocument(Document document);

    void refreshDocumentsFileList(Document document);

    Document loadDocument(String name, DocumentType docType);

}
