package com.strelnikov.doclib.repository;

import com.strelnikov.doclib.model.documnets.DocumentFile;
import com.strelnikov.doclib.model.documnets.DocumentVersion;

import java.util.List;

public interface DocFileDao {
    DocumentFile insertFile(DocumentFile file);

    void deleteFile(int fileId);

    List<DocumentFile> getFilesList(DocumentVersion documentVersion);

    DocumentFile getFile(int id);

    DocumentFile getFile(String name);
}
