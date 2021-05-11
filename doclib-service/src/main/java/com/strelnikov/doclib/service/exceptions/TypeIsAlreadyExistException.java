package com.strelnikov.doclib.service.exceptions;

import com.strelnikov.doclib.model.documnets.DocumentType;
import lombok.Getter;

@Getter
public class TypeIsAlreadyExistException extends  Exception{
    private DocumentType docType;

    public TypeIsAlreadyExistException(DocumentType docType){
        super("Type: "+docType.getCurentType()+" is already exist");
        this.docType = docType;
    }
}
