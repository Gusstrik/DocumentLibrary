package com.strelnikov.doclib.service;


import com.strelnikov.doclib.dto.DocTypeDto;
import com.strelnikov.doclib.service.exceptions.TypeIsAlreadyExistException;

public interface DocTypeService {
    DocTypeDto addDocumentType(DocTypeDto docTypeDto) throws TypeIsAlreadyExistException;

    void deleteDocumentType(int id);

    void refreshListDocumentType();
}
