package com.strelnikov.doclib.service.catalogs;

import com.strelnikov.doclib.service.conception.Entity;
import com.strelnikov.doclib.service.conception.Type;

public class Catalog extends Entity {

    public Catalog(String name) {
        super(name, Type.CATALOG);
    }
}

