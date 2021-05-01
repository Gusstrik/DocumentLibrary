package com.strelnikov.doclib.service.dtomapper;


import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.dto.DocTypeDto;


public interface DtoMapper {
    CatalogDto mapCatalog(String catalogName);

    void mapCatalog(CatalogDto catalog);

    DocTypeDto mapDocType();
}
