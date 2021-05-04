package com.strelnikov.doclib.service;

import com.strelnikov.doclib.dto.DocFileDto;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentFile;

public interface DocFileActions {

    DocFileDto createNewFile(DocFileDto docFileDto);

    void deleteFile(DocFileDto docFileDto);

}
