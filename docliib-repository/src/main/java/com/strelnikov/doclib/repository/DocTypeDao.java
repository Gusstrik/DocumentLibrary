package com.strelnikov.doclib.repository;

import com.strelnikov.doclib.model.documnets.DocumentType;

import java.util.List;

public interface DocTypeDao {
    void insertType(String type);

    void deleteType(String type);

    DocumentType loadType(int typeId);

    List<String> getTypesList();
}
