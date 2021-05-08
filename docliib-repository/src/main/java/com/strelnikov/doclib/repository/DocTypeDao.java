package com.strelnikov.doclib.repository;

import com.strelnikov.doclib.model.documnets.DocumentType;

import java.util.List;

public interface DocTypeDao {
    DocumentType insertType(DocumentType docType);

    void deleteType(int typeId);

    DocumentType loadType(int typeId);

    List<DocumentType> getTypesList();
}
