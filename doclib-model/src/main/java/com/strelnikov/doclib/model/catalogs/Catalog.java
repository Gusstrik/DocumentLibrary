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

    private List<Unit> contentList;

    public Catalog(){
        super();
        this.setUnitType(UnitType.CATALOG);
        contentList= new ArrayList<>();
    }

}

