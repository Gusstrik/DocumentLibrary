package com.strelnikov.doclib.service.dtomapper;


import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.dto.DocTypeDto;
import com.strelnikov.doclib.dto.DocumentDto;


public interface DtoMapper {
    CatalogDto mapCatalog(String catalogName);

    void mapCatalog(CatalogDto catalog);

    DocTypeDto mapDocType();

    DocumentDto mapDocument(DocumentDto documentDto);

    void mapNewDocument(DocumentDto documentDto);

    void mapNewDocVersion(DocumentDto documentDto);
}
