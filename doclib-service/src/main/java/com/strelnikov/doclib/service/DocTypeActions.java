package com.strelnikov.doclib.service;


import com.strelnikov.doclib.dto.DocTypeDto;

public interface DocTypeActions {
    void addDocumentType(DocTypeDto docTypeDto);

    void deleteDocumentType(DocTypeDto docTypeDto);

    void refreshListDocumentType();
}
