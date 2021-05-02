package com.strelnikov.doclib.service.impl;

import com.strelnikov.doclib.repository.DocumentDao;
import com.strelnikov.doclib.repository.FileDao;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentFile;
import com.strelnikov.doclib.service.DocumentFileActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentFileImpl implements DocumentFileActions {

    private final FileDao fileDao;
    private final DocumentDao documentDao;

    public DocumentFileImpl(@Autowired FileDao fileDao, @Autowired DocumentDao documentDao){
        this.fileDao=fileDao;
        this.documentDao=documentDao;
    }

    @Override
    public void createNewFile(DocumentFile file, Document document) {
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
