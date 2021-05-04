package com.strelnikov.doclib.service.exceptions;

import com.strelnikov.doclib.model.documnets.DocumentVersion;
import lombok.Getter;

@Getter
public class VersionIsAlreadyExistException extends Exception{
    private DocumentVersion docVer;

    public VersionIsAlreadyExistException(DocumentVersion docVer){
        super("Version " + docVer.getVersion()+" is already exitst in id: "+docVer.getId());
        this.docVer = docVer;
    }
}
