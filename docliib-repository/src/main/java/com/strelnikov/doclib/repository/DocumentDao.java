package com.strelnikov.doclib.repository;

import com.strelnikov.doclib.model.documnets.Document;

public interface DocumentDao {

    void addNewDocument(Document document, int catalogId);

    int getDocumentId(Document document);

    void deleteDocument(int documentId);

    Document loadDocument(String name, String type);
}
