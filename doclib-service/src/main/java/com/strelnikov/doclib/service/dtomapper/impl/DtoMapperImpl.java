package com.strelnikov.doclib.service.dtomapper.impl;

import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.dto.DocTypeDto;
import com.strelnikov.doclib.model.documnets.DocumentType;
import com.strelnikov.doclib.service.DocumentTypeActions;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.service.CatalogActions;
import com.strelnikov.doclib.service.impl.CatalogImpl;
import com.strelnikov.doclib.service.impl.DocumentTypeImpl;

public class DtoMapperImpl implements DtoMapper {

    public final CatalogActions catalogActions = new CatalogImpl();
    @Override
    public CatalogDto mapCatalog(String catalogName) {
        Catalog catalog = catalogActions.loadCatalog(catalogName);
        return new CatalogDto(catalog);
    }

    @Override
    public void mapCatalog(CatalogDto catalog) {
        if (catalog.getParent()==null||catalog.getParent().isEmpty()){
            catalogActions.createNewCatalog(catalog.getName());
        }else{
            catalogActions.createNewCatalog(catalog.getName(), catalog.getParent());
        }
    }

    @Override
    public DocTypeDto mapDocType() {
        DocumentTypeActions documentTypeActions = new DocumentTypeImpl();
        documentTypeActions.refreshListDocumentType();
        return new DocTypeDto(DocumentType.documentTypeList);
    }
}
