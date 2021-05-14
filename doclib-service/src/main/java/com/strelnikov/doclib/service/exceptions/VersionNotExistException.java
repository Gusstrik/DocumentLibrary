package com.strelnikov.doclib.service.exceptions;

import lombok.Getter;

@Getter
public class VersionNotExistException extends Exception {
    private int version;
    private int docId;

    public VersionNotExistException(int docId, int version){
        super("Version " + version + " doesn't exist for document with id = " + docId);
        this.docId=docId;
        this.version = version;
    }
}
