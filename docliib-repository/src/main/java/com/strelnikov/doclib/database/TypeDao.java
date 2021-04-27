package com.strelnikov.doclib.database;

import java.util.List;

public interface TypeDao {
    void addType(String type);

    void deleteType(String type);

    List<String> getTypesList();
}
