package com.strelnikov.doclib.service.impl;

import com.strelnikov.doclib.dto.DocVersionDto;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentVersion;
import com.strelnikov.doclib.repository.DocFileDao;
import com.strelnikov.doclib.repository.DocTypeDao;
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

    private final DocVersionDao docVersionDao;
    private final DtoMapper dtoMapper;
    private final DocumentDao documentDao;

    public DocVersionImpl(@Autowired DocVersionDao docVersionDao, @Autowired DtoMapper dtoMapper,
                          @Autowired DocumentDao documentDao) {
        this.docVersionDao = docVersionDao;
        this.dtoMapper = dtoMapper;
        this.documentDao=documentDao;
    }

    private boolean checkIsVersionExist(DocumentVersion documentVersion) {
        Document document = documentDao.loadDocument(documentVersion.getDocumentId());
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
            return dtoMapper.mapDocVersion(docVersionDao.insertDocVersion(dtoMapper.mapDocVersion(docVersionDto)));
        }
    }

    @Override
    public void deleteDocVersion(int versionId) {
        docVersionDao.deleteDocVersion(versionId);
    }
}
