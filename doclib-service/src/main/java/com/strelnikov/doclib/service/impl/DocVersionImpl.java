package com.strelnikov.doclib.service.impl;

import com.strelnikov.doclib.dto.DocVersionDto;
import com.strelnikov.doclib.repository.DocFileDao;
import com.strelnikov.doclib.repository.DocTypeDao;
import com.strelnikov.doclib.repository.DocVersionDao;
import com.strelnikov.doclib.service.DocVersionActions;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DocVersionImpl implements DocVersionActions {

    private final DocVersionDao docVersionDao;
    private final DtoMapper dtoMapper;

    public DocVersionImpl(@Autowired DocVersionDao docVersionDao, @Autowired DtoMapper dtoMapper) {
        this.docVersionDao = docVersionDao;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public DocVersionDto saveDocVersion(DocVersionDto docVersionDto) {
        return dtoMapper.mapDocVersion(docVersionDao.insertDocVersion(dtoMapper.mapDocVersion(docVersionDto)));
    }

    @Override
    public void deleteDocVersion(int versionId) {
        docVersionDao.deleteDocVersion(versionId);
    }
}
