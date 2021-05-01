package com.strelnikov.doclib.service.dtomapper;


import com.strelnikov.doclib.dto.CatalogDto;


public interface DtoMapper {
    CatalogDto mapCatalog(String catalogName);
    void mapCatalog(CatalogDto catalog);
}
