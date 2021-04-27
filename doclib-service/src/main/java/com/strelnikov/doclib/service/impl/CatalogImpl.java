package com.strelnikov.doclib.service.impl;

import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.service.CatalogActions;
import com.strelnikov.doclib.database.jdbc.CatalogDaoJdbc;

import java.util.ArrayList;
import java.util.List;

public class CatalogImpl implements CatalogActions {

    private final CatalogDaoJdbc catalogDaoJdbc = new CatalogDaoJdbc();
    @Override
    public Catalog createNewCatalog(String name){
        catalogDaoJdbc.addNewCatalog(name);
        return new Catalog(name);
    }

    @Override
    public void deleteCatalog(String name){
        catalogDaoJdbc.deleteCatalog(name);
    }

    @Override
    public List<String> showContent(Catalog catalog){
        catalog.setContentList(catalogDaoJdbc.getContentList(catalog.getName()));
        List<String> list = new ArrayList();
        for (Unit u:catalog.getContentList()){
            list.add(u.getName());
        }
        return list;
    }
}
