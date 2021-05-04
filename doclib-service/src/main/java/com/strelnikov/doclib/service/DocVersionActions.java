package com.strelnikov.doclib.service;

import com.strelnikov.doclib.dto.DocVersionDto;
import com.strelnikov.doclib.service.exceptions.VersionIsAlreadyExistException;

public interface DocVersionActions {
    DocVersionDto saveDocVersion(DocVersionDto docVersionDto) throws VersionIsAlreadyExistException;

    void deleteDocVersion(int versionId);
}
