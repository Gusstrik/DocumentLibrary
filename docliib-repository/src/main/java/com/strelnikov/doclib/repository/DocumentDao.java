package com.strelnikov.doclib.repository;

import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.model.documnets.Document;

import java.util.List;

public interface DocumentDao {

    Document insertDocument(Document document);

    Document loadDocument(int docId);

    void updateDocument(Document document);

    List<Unit> getDocumentsList(int catalogId);

    void deleteDocument(int documentId);


    Document findByName(String name);
}
