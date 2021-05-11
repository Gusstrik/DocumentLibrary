package com.strelnikov.doclib.service;


import com.strelnikov.doclib.dto.DocumentDto;
import com.strelnikov.doclib.service.exceptions.UnitIsAlreadyExistException;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import com.strelnikov.doclib.service.exceptions.VersionIsAlreadyExistException;

public interface DocumentActions {

    void deleteDocument(int documentId);

    DocumentDto loadDocument(int documentId) throws UnitNotFoundException;

    DocumentDto saveDocument(DocumentDto catalogDto) throws UnitIsAlreadyExistException, VersionIsAlreadyExistException;

    DocumentDto rollback(int id, int version) throws UnitNotFoundException, VersionIsAlreadyExistException;
}
