package com.strelnikov.doclib.database.Interface;

import com.strelnikov.doclib.model.conception.Entity;

import java.util.ArrayList;

public interface CatalogDao {

    void addNewCatalog(String name, String parent);

    void deleteCatalog(String name);

    ArrayList<Entity> getContentList(String catalogName);
}
