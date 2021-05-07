package com.strelnikov.doclib.service.impl;

import com.strelnikov.doclib.dto.DocVersionDto;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentFile;
import com.strelnikov.doclib.model.documnets.DocumentVersion;
import com.strelnikov.doclib.repository.DocVersionDao;
import com.strelnikov.doclib.repository.DocumentDao;
import com.strelnikov.doclib.service.DocVersionActions;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.service.exceptions.VersionIsAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DocVersionImpl implements DocVersionActions {

    @Autowired
    @Qualifier("DocVersionJpa")
    private  DocVersionDao docVersionDao;
    @Autowired
    private  DtoMapper dtoMapper;
    @Autowired
    private  DocFileImpl fileAct;


    private boolean checkIsVersionExist(DocumentVersion documentVersion) {
        Document document = documentVersion.getParentDocument();
        for (DocumentVersion docVer : document.getVersionsList()) {
            if (docVer.getVersion() == documentVersion.getVersion()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public DocVersionDto saveDocVersion(DocVersionDto docVersionDto) throws VersionIsAlreadyExistException {
        DocumentVersion documentVersion = dtoMapper.mapDocVersion(docVersionDto);
        if(checkIsVersionExist(documentVersion)){
            throw new VersionIsAlreadyExistException(documentVersion);
        }else {
            documentVersion = docVersionDao.insertDocVersion(documentVersion);
            for (DocumentFile docFile:documentVersion.getFilesList()){
                docFile.setDocVersion(documentVersion);
                docFile.setId(fileAct.createNewFile(dtoMapper.mapDocFile(docFile)).getId());
            }
            return dtoMapper.mapDocVersion(documentVersion);
        }
    }

    @Override
    public void deleteDocVersion(int versionId) {
        docVersionDao.deleteDocVersion(versionId);
    }
}
