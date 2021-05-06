package com.strelnikov.doclib.service.impl;

import com.strelnikov.doclib.dto.DocFileDto;
import com.strelnikov.doclib.repository.DocFileDao;
import com.strelnikov.doclib.service.DocFileActions;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DocFileImpl implements DocFileActions {

    private final DocFileDao docFileDao;
    private final DtoMapper dtoMapper;

    public DocFileImpl(@Autowired @Qualifier("DocFileJpa") DocFileDao docFileDao, @Autowired DtoMapper dtoMapper){
        this.docFileDao = docFileDao;
        this.dtoMapper=dtoMapper;
    }

    @Override
    public DocFileDto createNewFile(DocFileDto docFileDto) {
        return dtoMapper.mapDocFile(docFileDao.insertFile(dtoMapper.mapDocFile(docFileDto)));
    }

    @Override
    public void deleteFile(DocFileDto docFileDto) {
        docFileDao.deleteFile(docFileDto.getId());
    }
}
