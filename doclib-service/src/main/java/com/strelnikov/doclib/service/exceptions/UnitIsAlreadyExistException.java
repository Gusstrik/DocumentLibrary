package com.strelnikov.doclib.service.exceptions;

import com.strelnikov.doclib.model.conception.Unit;
import lombok.Getter;

@Getter
public class UnitIsAlreadyExistException extends Exception{

    private Unit parentUnit;

    private Unit addingUnit;

    public  UnitIsAlreadyExistException(Unit parentUnit, Unit addingUnit){
        super("Unit \""+addingUnit.getName()+"\" with the same type is already exist in \""+
                parentUnit.getName()+"\"");
        this.addingUnit = addingUnit;
        this.parentUnit=parentUnit;
    }
}
