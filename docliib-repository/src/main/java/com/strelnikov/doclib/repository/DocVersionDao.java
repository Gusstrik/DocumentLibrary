package com.strelnikov.doclib.repository;


import com.strelnikov.doclib.model.documnets.DocumentVersion;

public interface DocVersionDao {
    DocumentVersion loadDocVersion(int documentId, int docVersion);

    void addNewDocVersion(DocumentVersion documentVersion, int docuemntId);

    void deleteDocVersion(int versionId);

    int getDocVersionId(DocumentVersion documentVersion, int documentId);
}
