package com.strelnikov.doclib.model.documnets;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DocumentFile {

    private String fileName;

    private String filePath;

    public DocumentFile(String fileName, String filePath){
        this.fileName=fileName;
        this.filePath=filePath;
    }
}
