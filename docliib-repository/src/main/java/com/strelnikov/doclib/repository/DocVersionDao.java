package com.strelnikov.doclib.repository;


import com.strelnikov.doclib.model.documnets.DocumentFile;
import com.strelnikov.doclib.model.documnets.DocumentVersion;

import java.util.List;

public interface DocVersionDao {

    DocumentVersion insertDocVersion(DocumentVersion documentVersion);

    void deleteDocVersion(int versionId);

    List<DocumentVersion> getDocVersionList(int docId);

    DocumentVersion loadDocVersion(int docVerId);

    List<DocumentFile> getFileList(DocumentVersion docVersion);

}
