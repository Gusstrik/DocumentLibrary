package com.strelnikov.doclib.repository;

import com.strelnikov.doclib.model.documnets.DocumentFile;

import java.util.List;

public interface FileDao {
    void addNewFile(String fileName, int documentId, String filePath);

    void deleteFile(String name);

    DocumentFile loadFile(String Name);

    List<DocumentFile> getFilesList(int document_id);

    void copyFilesToNewDoc(List<DocumentFile> fileList, int docId);
}
