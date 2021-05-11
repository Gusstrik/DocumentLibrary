package com.strelnikov.doclib.service.exceptions;

import lombok.Getter;

@Getter
public class FileIsAlreadyExistException extends Exception{
    private String fileName;

    public FileIsAlreadyExistException(String fileName){
        super("File with name "+ fileName + " is already exist");
        this.fileName=fileName;
    }

}
