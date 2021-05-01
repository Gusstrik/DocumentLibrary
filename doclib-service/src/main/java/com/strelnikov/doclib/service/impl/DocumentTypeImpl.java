package com.strelnikov.doclib.service.impl;

import com.strelnikov.doclib.repository.jdbc.TypeDaoJdbc;
import com.strelnikov.doclib.model.documnets.DocumentType;
import com.strelnikov.doclib.service.DocumentTypeActions;

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
