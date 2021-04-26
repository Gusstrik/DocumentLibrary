package com.strelnikov.doclib.model.catalogs;

import com.strelnikov.doclib.model.conception.Entity;
import com.strelnikov.doclib.model.conception.Type;

public class Catalog extends Entity {

    public Catalog(String name) {
        super(name, Type.CATALOG);
    }
}

