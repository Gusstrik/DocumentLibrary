package com.strelnikov.doclib.service;

import com.strelnikov.doclib.dto.DocVersionDto;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import com.strelnikov.doclib.service.exceptions.VersionIsAlreadyExistException;

import java.io.FileNotFoundException;
import java.util.List;

public interface DocVersionService {

    DocVersionDto saveDocVersion(DocVersionDto docVersionDto) throws VersionIsAlreadyExistException, FileNotFoundException;

    void deleteDocVersion(int versionId);

    List<DocVersionDto> getVersionList(int docId) throws UnitNotFoundException;
}
