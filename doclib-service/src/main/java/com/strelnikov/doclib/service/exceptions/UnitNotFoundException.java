package com.strelnikov.doclib.service.exceptions;

import com.strelnikov.doclib.model.conception.Unit;
import lombok.Getter;

@Getter
public class UnitNotFoundException extends Exception{
    private int unitId;

    public UnitNotFoundException (int unitId){
        super("Unit with id = " + unitId+" not found");
        this.unitId = unitId;
    }
}
