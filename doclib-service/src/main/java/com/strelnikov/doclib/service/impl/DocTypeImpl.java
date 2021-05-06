package com.strelnikov.doclib.service.impl;

import com.strelnikov.doclib.dto.DocTypeDto;
import com.strelnikov.doclib.repository.CatalogDao;
import com.strelnikov.doclib.repository.DocTypeDao;
import com.strelnikov.doclib.model.documnets.DocumentType;
import com.strelnikov.doclib.service.DocTypeActions;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DocTypeImpl implements DocTypeActions {

    private final DocTypeDao docTypeDao;
    private final DtoMapper dtoMapper;

    public DocTypeImpl(@Qualifier("DocTypeJpa") DocTypeDao docTypeDao, @Autowired DtoMapper dtoMapper) {
        this.docTypeDao = docTypeDao;
        this.dtoMapper = dtoMapper;
    }


    @Override
    public void addDocumentType(DocTypeDto docTypeDto) {
        docTypeDao.insertType(docTypeDto.getDocType());
        refreshListDocumentType();

    }

    @Override
    public void deleteDocumentType(DocTypeDto docTypeDto) {
        docTypeDao.deleteType(docTypeDto.getDocType());
        refreshListDocumentType();
    }

    @Override
    public void refreshListDocumentType() {
        DocumentType.documentTypeList=docTypeDao.getTypesList();
        DocTypeDto.typesList=docTypeDao.getTypesList();
    }
}
