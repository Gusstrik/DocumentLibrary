package com.strelnikov.doclib.repository;


import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentVersion;

import java.util.List;

public interface DocVersionDao {

    DocumentVersion insertDocVersion(DocumentVersion documentVersion);

    void deleteDocVersion(int versionId);

    List<DocumentVersion> getDocVersionList(Document document);

    DocumentVersion loadDocVersion(int docVerId);

}
