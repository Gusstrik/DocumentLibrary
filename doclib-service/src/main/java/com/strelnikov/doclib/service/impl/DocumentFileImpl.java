package com.strelnikov.doclib.service.impl;

import com.strelnikov.doclib.repository.DocumentDao;
import com.strelnikov.doclib.repository.FileDao;
import com.strelnikov.doclib.repository.jdbc.DocumentDaoJdbc;
import com.strelnikov.doclib.repository.jdbc.FileDaoJdbc;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentFile;
import com.strelnikov.doclib.service.DocumentFileActions;

public class DocumentFileImpl implements DocumentFileActions {

    private final FileDao fileDao = new FileDaoJdbc();

    @Override
    public void createNewFile(DocumentFile file, Document document) {
        DocumentDao documentDao = new DocumentDaoJdbc();
        int documentId = documentDao.getDocumentId(document);
        fileDao.addNewFile(file.getFileName(),documentId, file.getFilePath());
    }

    @Override
    public void deleteFile(DocumentFile file) {
        fileDao.deleteFile(file.getFileName());
    }

    @Override
    public DocumentFile loadFile(String name) {
        return fileDao.loadFile(name);
    }
}
