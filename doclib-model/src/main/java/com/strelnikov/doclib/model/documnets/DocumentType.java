package com.strelnikov.doclib.model.documnets;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DocumentType {
    public static List<String> documentTypeList;

    private String curentType;

    public  DocumentType(){

    }

    public DocumentType(String type){
        curentType=type;
    }
}
