package com.strelnikov.doclib.database;

public interface DocumentDao {

    void addNewDocument(String name, String type, int version, String description, int catalogId);
    int getDocumentId(String name, String type, int version);
    void deleteDocument(int id);
}
