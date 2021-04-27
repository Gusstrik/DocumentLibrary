package com.strelnikov.doclib.service.impl;

import com.strelnikov.doclib.database.jdbc.TypeDaoJdbc;
import com.strelnikov.doclib.model.documnets.DocumentType;
import com.strelnikov.doclib.service.DocumentTypeActions;

import java.util.List;

public class DocumentTypeImpl implements DocumentTypeActions {

    @Override
    public void addDocumentType(String typeName) {
        TypeDaoJdbc typeDaoJdbc = new TypeDaoJdbc();
        typeDaoJdbc.addType(typeName);
        DocumentType.documentTypeList.add(typeName);
    }

    @Override
    public void deleteDocumentType(String typeName) {
        TypeDaoJdbc typeDaoJdbc = new TypeDaoJdbc();
        typeDaoJdbc.deleteType(typeName);
        DocumentType.documentTypeList.remove(typeName);
    }

    @Override
    public void refreshListDocumentType() {
        TypeDaoJdbc typeDaoJdbc = new TypeDaoJdbc();
        DocumentType.documentTypeList=typeDaoJdbc.getTypesList();
    }
}
