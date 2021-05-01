package com.strelnikov.doclib.service.impl;

import com.strelnikov.doclib.repository.CatalogDao;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.service.CatalogActions;
import com.strelnikov.doclib.repository.jdbc.CatalogDaoJdbc;

import java.util.ArrayList;
import java.util.List;

public class CatalogImpl implements CatalogActions {

    private final CatalogDao catalogDao = new CatalogDaoJdbc();

    @Override
    public Catalog createNewCatalog(String name, String parent){
        catalogDao.addNewCatalog(name, parent);
        return new Catalog(name);
    }

    @Override
    public Catalog createNewCatalog(String name) {
        return createNewCatalog(name,"/");
    }

    @Override
    public void deleteCatalog(String name){
        catalogDao.deleteCatalog(name);
    }

    @Override
    public List<String> showContent(Catalog catalog){
        catalog.setContentList(catalogDao.getContentList(catalog.getName()));
        List<String> list = new ArrayList();
        for (Unit u:catalog.getContentList()){
            list.add(u.getName());
        }
        return list;
    }

    @Override
    public Catalog loadCatalog(String name) {
        Catalog catalog = catalogDao.loadCatalog(name);
        catalog.setContentList(catalogDao.getContentList(catalog.getName()));
        return catalog;
    }
}
