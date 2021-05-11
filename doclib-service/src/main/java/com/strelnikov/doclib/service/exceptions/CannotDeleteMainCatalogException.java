package com.strelnikov.doclib.service.exceptions;

public class CannotDeleteMainCatalogException extends Exception {
    public CannotDeleteMainCatalogException(){
        super("Can't delete main catalog with id = 1");
    }
}
