package com.strelnikov.doclib.service;

import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.dto.UnitDto;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.model.roles.Client;
import com.strelnikov.doclib.model.roles.PermissionType;
import com.strelnikov.doclib.service.exceptions.CannotDeleteMainCatalogException;
import com.strelnikov.doclib.service.exceptions.UnitIsAlreadyExistException;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;

public interface CatalogService {

    void deleteCatalog(CatalogDto catalogDto) throws CannotDeleteMainCatalogException;

    CatalogDto loadCatalog(int catalogId) throws UnitNotFoundException;

    CatalogDto saveCatalog (CatalogDto catalogDto) throws UnitIsAlreadyExistException;

    CatalogDto filterContentList(CatalogDto catalogDto, String login, PermissionType permissionType);

}
