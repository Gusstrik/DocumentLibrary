package com.strelnikov.doclib.service;

import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentFile;

public interface DocumentFileActions {

    void createNewFile(DocumentFile file, Document document);

    void deleteFile(DocumentFile file);

    DocumentFile loadFile(String name);
}
