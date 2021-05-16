package com.strelnikov.doclib.service;


import com.strelnikov.doclib.dto.DocumentDto;
import com.strelnikov.doclib.service.exceptions.UnitIsAlreadyExistException;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import com.strelnikov.doclib.service.exceptions.VersionIsAlreadyExistException;
import com.strelnikov.doclib.service.exceptions.VersionNotExistException;

import java.io.FileNotFoundException;

public interface DocumentService {

    void deleteDocument(int documentId);

    DocumentDto loadDocument(int documentId, int version) throws UnitNotFoundException, VersionNotExistException;

    DocumentDto loadDocument(int documentId) throws UnitNotFoundException;

    DocumentDto saveDocument(DocumentDto catalogDto) throws UnitIsAlreadyExistException, VersionIsAlreadyExistException, FileNotFoundException, VersionNotExistException;

    DocumentDto rollback(int id, int version) throws UnitNotFoundException, VersionNotExistException;
}
