package com.strelnikov.doclib.repository;

import com.strelnikov.doclib.model.documnets.DocumentType;

import java.util.List;

public interface DocTypeDao {
    void addType(String type);

    void deleteType(String type);

    List<String> getTypesList();
}
