package com.strelnikov.doclib.model.catalogs;

import com.strelnikov.doclib.model.conception.Unit;

import com.strelnikov.doclib.model.conception.UnitType;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

@Entity
@Table(name="catalogs")
public class Catalog extends Unit {

    @Transient
    private List<Unit> contentList;

    public Catalog(){
        super();
        this.setUnitType(UnitType.CATALOG);
        contentList= new ArrayList<>();
    }

    public boolean containUnit(Unit unit){
        for (Unit contain:this.contentList){
            if (contain.equals(unit)){
                return true;
            }
        }
        return false;
    }

}

