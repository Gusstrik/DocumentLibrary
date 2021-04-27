package com.strelnikov.doclib.database;

import com.strelnikov.doclib.model.conception.Unit;

import java.util.List;

public interface CatalogDao {

    void addNewCatalog(String name, String parent);

    void deleteCatalog(String name);

    List<Unit> getContentList(String catalogName);
}
