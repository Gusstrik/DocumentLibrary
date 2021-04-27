package com.strelnikov.doclib.model.catalogs;

import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.model.conception.UnitType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Catalog extends Unit {

    private List<Unit> ContentList;

    public Catalog(String name) {
        super(name, UnitType.CATALOG);
        ContentList = new ArrayList();
    }

}

