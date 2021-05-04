package com.strelnikov.doclib.service;

import com.strelnikov.doclib.dto.DocVersionDto;

public interface DocVersionActions {
    DocVersionDto saveDocVersion(DocVersionDto docVersionDto);

    void deleteDocVersion(int versionId);
}
