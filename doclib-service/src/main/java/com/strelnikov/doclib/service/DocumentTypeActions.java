package com.strelnikov.doclib.service;

import java.util.List;

public interface DocumentTypeActions {
    void addDocumentType(String typeName);
    void deleteDocumentType(String typeName);
    void  refreshListDocumentType();
}
