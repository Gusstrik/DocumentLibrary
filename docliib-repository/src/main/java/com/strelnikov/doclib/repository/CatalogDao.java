package com.strelnikov.doclib.repository;

import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.conception.Unit;

import java.util.List;

public interface CatalogDao {

    void addNewCatalog(Catalog catalog);

    Catalog loadCatalog(String name);

    void deleteCatalog(String name);

    int  getCatalogId(String name);

}
