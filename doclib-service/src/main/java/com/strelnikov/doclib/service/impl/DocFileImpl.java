package com.strelnikov.doclib.service.impl;

import com.strelnikov.doclib.dto.DocFileDto;
import com.strelnikov.doclib.model.documnets.DocumentFile;
import com.strelnikov.doclib.repository.DocFileDao;
import com.strelnikov.doclib.service.DocFileActions;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.service.exceptions.FileIsAlreadyExistException;
import com.strelnikov.doclib.service.exceptions.UnitIsAlreadyExistException;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class DocFileImpl implements DocFileActions {

    private final DocFileDao docFileDao;
    private final DtoMapper dtoMapper;

    public DocFileImpl(@Autowired @Qualifier("DocFileJpa") DocFileDao docFileDao, @Autowired DtoMapper dtoMapper){
        this.docFileDao = docFileDao;
        this.dtoMapper=dtoMapper;
    }

    private boolean isFileExist(DocumentFile docFile){
        docFile = docFileDao.getFile(docFile.getFileName());
        if (docFile==null){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public DocFileDto createNewFile(DocFileDto docFileDto) throws FileIsAlreadyExistException {
        DocumentFile docFile = dtoMapper.mapDocFile(docFileDto);
        if (isFileExist(docFile)){
            throw new FileIsAlreadyExistException(docFile.getFileName());
        }else{
            return dtoMapper.mapDocFile(docFileDao.insertFile(docFile));
        }
    }

    @Override
    public void deleteFile(int id) throws UnitNotFoundException {
        DocumentFile docFile = docFileDao.getFile(id);
        if (docFile!=null){
            docFileDao.deleteFile(id);
            File file = new File(docFile.getFilePath());
            file.delete();
        }else{
            throw new UnitNotFoundException(id);
        }
    }

    @Override
    public DocFileDto loadFile(String name) throws UnitNotFoundException {
        DocumentFile docFile = docFileDao.getFile(name);
        if (docFile == null){
            throw new UnitNotFoundException(0);
        }else{
            return dtoMapper.mapDocFile(docFile);
        }
    }
}
