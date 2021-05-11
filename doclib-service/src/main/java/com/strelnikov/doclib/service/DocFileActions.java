package com.strelnikov.doclib.service;

import com.strelnikov.doclib.dto.DocFileDto;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentFile;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;

public interface DocFileActions {

    DocFileDto createNewFile(DocFileDto docFileDto);

    void deleteFile(int id) throws UnitNotFoundException;


}
