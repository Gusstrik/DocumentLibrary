package com.strelnikov.doclib.repository;

import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.conception.Unit;

import java.util.List;

public interface CatalogDao {

    void addNewCatalog(String name, String parent);

    Catalog loadCatalog(String name);

    void deleteCatalog(String name);

    int  getCatalogId(String name);

    List<Unit> getContentList(String catalogName);
}
