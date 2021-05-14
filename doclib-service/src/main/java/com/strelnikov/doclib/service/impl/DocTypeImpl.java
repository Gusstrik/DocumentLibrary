package com.strelnikov.doclib.service.impl;

import com.strelnikov.doclib.dto.DocTypeDto;
import com.strelnikov.doclib.repository.DocTypeDao;
import com.strelnikov.doclib.model.documnets.DocumentType;
import com.strelnikov.doclib.service.DocTypeActions;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.service.exceptions.TypeIsAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.ArrayList;

@Service
public class DocTypeImpl implements DocTypeActions {

    private final DocTypeDao docTypeDao;
    private final DtoMapper dtoMapper;

    public DocTypeImpl(@Qualifier("DocTypeJpa") DocTypeDao docTypeDao, @Autowired DtoMapper dtoMapper) {
        this.docTypeDao = docTypeDao;
        this.dtoMapper = dtoMapper;
    }



    @Override
    public DocTypeDto addDocumentType(DocTypeDto docTypeDto) throws TypeIsAlreadyExistException {
        DocumentType docType = dtoMapper.mapDocType(docTypeDto);
        if(docType.isTypeExist()){
            throw new TypeIsAlreadyExistException(docType);
        }else{
            docType=docTypeDao.insertType(docType);
            DocumentType.documentTypeList.add(docType);
            docTypeDto = dtoMapper.mapDocType(docType);
            DocTypeDto.typesList.add(docTypeDto);
            return docTypeDto;
        }


    }

    @Override
    public void deleteDocumentType(int id) {
        docTypeDao.deleteType(id);
        refreshListDocumentType();
    }

    @Override
    public void refreshListDocumentType() {
        DocumentType.documentTypeList=docTypeDao.getTypesList();
        DocTypeDto.typesList=new ArrayList<>();
        for (DocumentType docType:DocumentType.documentTypeList){
            DocTypeDto.typesList.add(dtoMapper.mapDocType(docType));
        }
    }
}
