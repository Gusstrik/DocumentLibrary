package com.strelnikov.doclib.service.impl;

import com.strelnikov.doclib.repository.TypeDao;
import com.strelnikov.doclib.model.documnets.DocumentType;
import com.strelnikov.doclib.service.DocumentTypeActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentTypeImpl implements DocumentTypeActions {

    private final TypeDao typeDao;

    public DocumentTypeImpl(@Autowired TypeDao typeDao){
        this.typeDao=typeDao;
    }

    @Override
    public void addDocumentType(String typeName) {
        typeDao.addType(typeName);
        DocumentType.documentTypeList.add(typeName);
    }

    @Override
    public void deleteDocumentType(String typeName) {
        typeDao.deleteType(typeName);
        DocumentType.documentTypeList.remove(typeName);
    }

    @Override
    public void refreshListDocumentType() {
        DocumentType.documentTypeList=typeDao.getTypesList();
    }
}
