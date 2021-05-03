package com.strelnikov.doclib.repository;

import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.conception.Unit;

import java.util.List;

public interface CatalogDao {

    void updateCatalog(Catalog catalog);

    Catalog loadCatalog(int catalogId);

    void deleteCatalog(int catalogId);

    Catalog insertCatalog(Catalog catalog);

}
