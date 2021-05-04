package com.strelnikov.doclib.service;

import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.dto.UnitDto;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.service.exceptions.UnitIsAlreadyExistException;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;

public interface CatalogActions {

    void deleteCatalog(CatalogDto catalogDto);

    CatalogDto loadCatalog(int catalogId) throws UnitNotFoundException;

    CatalogDto saveCatalog (CatalogDto catalogDto) throws UnitIsAlreadyExistException;

}
