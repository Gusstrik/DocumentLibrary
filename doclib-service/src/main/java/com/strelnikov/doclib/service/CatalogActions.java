package com.strelnikov.doclib.service;

import com.strelnikov.doclib.model.catalogs.Catalog;

import java.util.List;

public interface CatalogActions {
    Catalog createNewCatalog(String name, String parent);

    Catalog createNewCatalog(String name);

    void deleteCatalog(String name);

    List<String> showContent(Catalog catalog);

    Catalog loadCatalog(String name);
}
