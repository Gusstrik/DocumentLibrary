package com.strelnikov.doclib.service.impl;

import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.dto.UnitDto;
import com.strelnikov.doclib.model.conception.UnitType;
import com.strelnikov.doclib.repository.DocumentDao;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.service.exceptions.CannotDeleteMainCatalogException;
import com.strelnikov.doclib.service.exceptions.UnitIsAlreadyExistException;
import com.strelnikov.doclib.repository.CatalogDao;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.service.CatalogActions;

import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CatalogImpl implements CatalogActions {

    private final CatalogDao catalogDao;
    private final DocumentDao documentDao;
    private final DtoMapper dtoMapper;

    public CatalogImpl(@Qualifier("CatalogJpa") CatalogDao catalogDao, @Autowired DtoMapper dtoMapper,
                       @Qualifier("DocumentJpa") DocumentDao documentDao) {
        this.catalogDao = catalogDao;
        this.dtoMapper = dtoMapper;
        this.documentDao = documentDao;
    }


    private boolean checkIfCatalogExist(Unit addingUnit) {
        if (addingUnit.getId() != 0 && addingUnit.getCatalogId() != 0) {
            Catalog parentCatlog = catalogDao.loadCatalog(addingUnit.getCatalogId());
            for (Unit unit : parentCatlog.getContentList()) {
                if (unit.getUnitType().equals(UnitType.CATALOG) &&
                        unit.getName().equals(addingUnit.getName()) &&
                        unit.getId() != addingUnit.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    private CatalogDto createNewCatalog(CatalogDto catalogDto) throws UnitIsAlreadyExistException {
        Catalog catalog = dtoMapper.mapCatalog(catalogDto);
        if (checkIfCatalogExist(catalog)) {
            throw new UnitIsAlreadyExistException(catalogDao.loadCatalog(catalog.getCatalogId()), catalog);
        } else {
            return dtoMapper.mapCatalog(catalogDao.insertCatalog(catalog));
        }
    }

    @Override
    public void deleteCatalog(CatalogDto catalogDto) throws CannotDeleteMainCatalogException {
        if (catalogDto.getId() != 1) {
            catalogDao.deleteCatalog(catalogDto.getId());
        }
        else {
            throw new CannotDeleteMainCatalogException();
        }
    }

    @Override
    public CatalogDto loadCatalog(int catalogId) throws UnitNotFoundException {
        Catalog catalog = catalogDao.loadCatalog(catalogId);
        if (catalog == null) {
            throw new UnitNotFoundException(catalogId);
        } else {
            return dtoMapper.mapCatalog(catalogDao.loadCatalog(catalogId));
        }
    }

    @Override
    public CatalogDto saveCatalog(CatalogDto catalogDto) throws UnitIsAlreadyExistException {
        try {
            loadCatalog(catalogDto.getId());
            editCatalog(dtoMapper.mapCatalog(catalogDto));
        } catch (UnitNotFoundException e) {
            catalogDto = createNewCatalog(catalogDto);
        }
        return catalogDto;
    }

    private void editCatalog(Catalog catalog) throws UnitIsAlreadyExistException {
        if (checkIfCatalogExist(catalog)) {
            throw new UnitIsAlreadyExistException(catalogDao.loadCatalog(catalog.getCatalogId()), catalog);
        } else {
            deleteContent(catalog);
            catalogDao.updateCatalog(catalog);
        }
    }

    private void deleteUnit(Unit unit) {
        if (unit.getUnitType().equals(UnitType.CATALOG)) {
            catalogDao.deleteCatalog(unit.getId());
        } else {
            documentDao.deleteDocument(unit.getId());
        }
    }

    private List<Unit> deleteContent(Catalog inputCat) {
        List<Unit> list = new ArrayList<>();
        Catalog catalogDb = catalogDao.loadCatalog(inputCat.getId());
        for (Unit unit : catalogDb.getContentList()) {
            if (!inputCat.containUnit(unit)) {
                deleteUnit(unit);
            }
        }
        return list;
    }


}
