package com.strelnikov.doclib.model.documnets;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DocumentFile {

    private int id;

    private String fileName;

    private int docVersionId;

    private String filePath;

    public DocumentFile(){

    }

    public DocumentFile(String fileName, String filePath){
        this.fileName=fileName;
        this.filePath=filePath;
    }
}
