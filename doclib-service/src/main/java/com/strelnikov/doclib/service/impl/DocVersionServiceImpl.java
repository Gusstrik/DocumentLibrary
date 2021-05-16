package com.strelnikov.doclib.service.impl;

import com.strelnikov.doclib.dto.DocVersionDto;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentFile;
import com.strelnikov.doclib.model.documnets.DocumentVersion;
import com.strelnikov.doclib.repository.DocVersionDao;
import com.strelnikov.doclib.repository.DocumentDao;
import com.strelnikov.doclib.service.DocFileService;
import com.strelnikov.doclib.service.DocVersionService;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import com.strelnikov.doclib.service.exceptions.VersionIsAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocVersionServiceImpl implements DocVersionService {

    private final   DocVersionDao docVersionDao;
    private final   DtoMapper dtoMapper;
    private final DocFileService fileAct;
    private final DocumentDao docDao;

    public DocVersionServiceImpl(@Qualifier("DocVersionJpa")DocVersionDao docVersionDao, @Autowired DtoMapper dtoMapper,
                                 @Autowired DocFileService fileAct, @Autowired DocumentDao docDao){
        this.dtoMapper=dtoMapper;
        this.docDao=docDao;
        this.fileAct =fileAct;
        this.docVersionDao = docVersionDao;
    }


    private boolean checkIsVersionExist(DocumentVersion documentVersion) {
        Document document = documentVersion.getParentDocument();
        for (DocumentVersion docVer : document.getVersionsList()) {
            if (docVer.getVersion() == documentVersion.getVersion()) {
                return true;
            }
        }
        return false;
    }

    private boolean checkAreFilesExist(DocumentVersion docVersion){
        for (DocumentFile docFile:docVersion.getFilesList()){
            if (!fileAct.isFileExist(docFile)){
                return false;
            }
        }
        return true;
    }


    @Override
    public DocVersionDto saveDocVersion(DocVersionDto docVersionDto) throws VersionIsAlreadyExistException, FileNotFoundException {
        DocumentVersion documentVersion = dtoMapper.mapDocVersion(docVersionDto);
        if(checkIsVersionExist(documentVersion)){
            throw new VersionIsAlreadyExistException(documentVersion);
        }else {
            if(checkAreFilesExist(documentVersion)) {
                documentVersion = docVersionDao.insertDocVersion(documentVersion);

                return dtoMapper.mapDocVersion(documentVersion);
            }else{
                throw new FileNotFoundException();
            }
        }
    }

    @Override
    public void deleteDocVersion(int versionId) {
        DocumentVersion documentVersion = docVersionDao.loadDocVersion(versionId);
        if(documentVersion!=null) {
            Document document = documentVersion.getParentDocument();
            List<DocumentVersion> docVerList = docVersionDao.getDocVersionList(document.getId());
            for (int i = documentVersion.getVersion() + 1; i <= document.getActualVersion(); i++) {
                docVerList.get(i).setVersion(i - 1);
                docVersionDao.updateVersion(docVerList.get(i));
            }
            docVersionDao.deleteDocVersion(versionId);
            document.setActualVersion(document.getActualVersion()-1);
            docDao.updateDocument(document);
        }
    }

    @Override
    public List<DocVersionDto> getVersionList(int docId) throws UnitNotFoundException {
        List<DocumentVersion> docVerList = docVersionDao.getDocVersionList(docId);
        if(docVerList!=null) {
            List<DocVersionDto> docVerDtoList = new ArrayList<>();
            for (DocumentVersion docVer : docVerList) {
                docVerDtoList.add(dtoMapper.mapDocVersion(docVer));
            }
            return docVerDtoList;
        }else{
            throw new UnitNotFoundException(docId);
        }
    }
}
