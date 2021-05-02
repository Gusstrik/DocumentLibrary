package com.strelnikov.doclib.service;


public interface DocumentTypeActions {
    void addDocumentType(String typeName);

    void deleteDocumentType(String typeName);

    void refreshListDocumentType();
}
