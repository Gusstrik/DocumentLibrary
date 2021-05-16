package com.strelnikov.doclib.service.impl;

import com.strelnikov.doclib.dto.DocumentDto;
import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.model.conception.UnitType;
import com.strelnikov.doclib.model.documnets.DocumentFile;
import com.strelnikov.doclib.repository.CatalogDao;

import com.strelnikov.doclib.repository.DocFileDao;
import com.strelnikov.doclib.repository.DocumentDao;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.documnets.Document;

import com.strelnikov.doclib.model.documnets.DocumentVersion;
import com.strelnikov.doclib.service.DocVersionService;
import com.strelnikov.doclib.service.DocumentService;
import com.strelnikov.doclib.service.SecurityService;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.service.exceptions.UnitIsAlreadyExistException;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import com.strelnikov.doclib.service.exceptions.VersionIsAlreadyExistException;
import com.strelnikov.doclib.service.exceptions.VersionNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentDao documentDao;
    private final CatalogDao catalogDao;
    private final DtoMapper dtoMapper;
    private final DocVersionService docVerActions;
    private final DocFileDao docFileDao;
    private final SecurityService securityService;

    public DocumentServiceImpl(@Qualifier("DocumentJpa") DocumentDao documentDao, @Autowired DocVersionService docVerActions,
                               @Autowired DtoMapper dtoMapper, @Qualifier("CatalogJpa") CatalogDao catalogDao, @Qualifier("DocFileJpa") DocFileDao docFileDao,
                               @Autowired SecurityService securityService) {
        this.documentDao = documentDao;
        this.docVerActions = docVerActions;
        this.dtoMapper = dtoMapper;
        this.catalogDao = catalogDao;
        this.docFileDao = docFileDao;
        this.securityService = securityService;
    }

    @Override
    public void deleteDocument(int documentId) {
        securityService.removeObjectFromSecureTable(documentDao.loadDocument(documentId));
        documentDao.deleteDocument(documentId);
    }

    @Override
    public DocumentDto loadDocument(int documentId, int version) throws UnitNotFoundException, VersionNotExistException {
        Document document = documentDao.loadDocument(documentId);
        if (document == null) {
            throw new UnitNotFoundException(documentId);
        } else if (version > document.getActualVersion()) {
            throw new VersionNotExistException(documentId, version);
        } else {
            for (DocumentVersion documentVersion : document.getVersionsList()) {
                documentVersion.setFilesList(docFileDao.getFilesList(documentVersion));
            }
            return dtoMapper.mapDocument(document, version);
        }
    }

    @Override
    public DocumentDto loadDocument(int documentId) throws UnitNotFoundException {
        Document document = documentDao.loadDocument(documentId);
        if (document == null) {
            throw new UnitNotFoundException(documentId);
        } else {
            for (DocumentVersion documentVersion : document.getVersionsList()) {
                documentVersion.setFilesList(docFileDao.getFilesList(documentVersion));
            }
            return dtoMapper.mapDocument(document);
        }
    }

    private boolean checkIfDocumentExist(Document addingDocuemnt) {
        Catalog parentCatlog = catalogDao.loadCatalog(addingDocuemnt.getCatalogId());
        for (Unit unit : parentCatlog.getContentList()) {
            if (unit.getUnitType().equals(UnitType.DOCUMENT)) {
                Document existinDoc = documentDao.loadDocument(unit.getId());
                if (existinDoc.getName().equals(addingDocuemnt.getName()) &&
                        existinDoc.getDocumentType().getCurentType().equals(addingDocuemnt.getDocumentType().getCurentType()) &&
                        unit.getId() != addingDocuemnt.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    private DocumentDto createNewDocument(DocumentDto documentDto) throws UnitIsAlreadyExistException, VersionIsAlreadyExistException, FileNotFoundException {
        Document document = dtoMapper.mapDocument(documentDto);
        if (checkIfDocumentExist(document)) {
            throw new UnitIsAlreadyExistException(catalogDao.loadCatalog(document.getCatalogId()), document);
        } else {
            document = documentDao.insertDocument(document);
            DocumentVersion docVer = document.getDocumentVersion();
            docVer.setParentDocument(document);
            docVer.setId(docVerActions.saveDocVersion(dtoMapper.mapDocVersion(docVer)).getId());
            securityService.addObjectToSecureTable(document);
            securityService.inheritPermissions(document,catalogDao.loadCatalog(document.getCatalogId()));
            for (DocumentFile documentFile:docVer.getFilesList()){
                securityService.inheritPermissions(documentFile, docVer.getParentDocument());
            }
            return dtoMapper.mapDocument(document);
        }
    }

    private void editDocument(DocumentDto documentDto) throws VersionIsAlreadyExistException, FileNotFoundException, UnitNotFoundException, VersionNotExistException {
        Document documentDb = documentDao.loadDocument(documentDto.getId());
        Document document = dtoMapper.mapDocument(documentDto);
        if (documentDb.getActualVersion() < document.getActualVersion()) {
            document.setActualVersion(documentDb.getActualVersion() + 1);
            DocumentVersion docVer = document.getVersionsList().get(0);
            docVer.setVersion(document.getActualVersion());
            docVerActions.saveDocVersion(dtoMapper.mapDocVersion(docVer));
            for (DocumentFile documentFile:docVer.getFilesList()){
                securityService.inheritPermissions(documentFile, docVer.getParentDocument());
            }
        }
        if (documentDb.getActualVersion() > document.getActualVersion()) {
            rollback(documentDb.getId(), document.getActualVersion());
        }
        documentDao.updateDocument(document);
    }

    @Override
    public DocumentDto saveDocument(DocumentDto documentDto) throws UnitIsAlreadyExistException, VersionIsAlreadyExistException, FileNotFoundException, VersionNotExistException {
        try {
            loadDocument(documentDto.getId());
            editDocument(documentDto);
            documentDto = loadDocument(documentDto.getId());
        } catch (UnitNotFoundException e) {
            documentDto = createNewDocument(documentDto);
        }
        return documentDto;
    }

    @Override
    public DocumentDto rollback(int id, int version) throws UnitNotFoundException, VersionNotExistException {
        Document document = documentDao.loadDocument(id);
        if (document == null) {
            throw new UnitNotFoundException(id);
        } else {
            if (version > document.getActualVersion()) {
                throw new VersionNotExistException(id, version);
            }
            for (int i = document.getActualVersion(); i > version; i--) {
                DocumentVersion docVer = document.getDocumentVersion(i);
                docVerActions.deleteDocVersion(docVer.getId());
            }
            document.setActualVersion(version);
            documentDao.updateDocument(document);
            return loadDocument(id);
        }
    }
}
