package com.strelnikov.doclib.model.catalogs;

import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.model.conception.Type;

public class Catalog extends Unit {

    public Catalog(String name) {
        super(name, Type.CATALOG);
    }
}

