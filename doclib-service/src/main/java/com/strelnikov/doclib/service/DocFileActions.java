package com.strelnikov.doclib.service;

import com.strelnikov.doclib.dto.DocFileDto;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentFile;
import com.strelnikov.doclib.service.exceptions.FileIsAlreadyExistException;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;

public interface DocFileActions {

    boolean isFileExist(DocumentFile docFile);

    DocFileDto createNewFile(DocFileDto docFileDto) throws FileIsAlreadyExistException;

    void deleteFile(int id) throws UnitNotFoundException;

    DocFileDto loadFile(String name) throws UnitNotFoundException;
}
