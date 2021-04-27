package com.strelnikov.doclib.database;

import java.util.List;

public interface FileDao {
    void addNewFile(String fileName, int documentId, String filePath);

    void deleteFile(String name);

    void renameFile(String newName, String oldName);

    List<String> getFilesList(int document_id);
}
