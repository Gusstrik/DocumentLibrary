package com.strelnikov.doclib.model.roles;

import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentFile;

public class FactorySecuredObject {
    public static SecuredObject createSecuredObject(Object object){
        switch (object.getClass().getSimpleName()){
            case "Catalog":
                return (Catalog)object;

            case "Document":
                return (Document)object;

            case "DocumentFile":
                return (DocumentFile)object;

        }
        return null;
    }
}
