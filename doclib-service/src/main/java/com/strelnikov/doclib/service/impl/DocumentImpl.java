package com.strelnikov.doclib.service.impl;

import com.strelnikov.doclib.dto.DocumentDto;
import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.model.conception.UnitType;
import com.strelnikov.doclib.repository.CatalogDao;

import com.strelnikov.doclib.repository.DocFileDao;
import com.strelnikov.doclib.repository.DocumentDao;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.documnets.Document;

import com.strelnikov.doclib.model.documnets.DocumentVersion;
import com.strelnikov.doclib.service.DocVersionActions;
import com.strelnikov.doclib.service.DocumentActions;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.service.exceptions.UnitIsAlreadyExistException;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import com.strelnikov.doclib.service.exceptions.VersionIsAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentImpl implements DocumentActions {

    private final DocumentDao documentDao;
    private final CatalogDao catalogDao;
    private final DtoMapper dtoMapper;
    private final DocVersionActions docVerActions;
    private final DocFileDao docFileDao;

    public DocumentImpl(@Autowired DocumentDao documentDao, @Autowired DocVersionActions docVerActions,
                        @Autowired DtoMapper dtoMapper, @Autowired CatalogDao catalogDao, @Autowired DocFileDao docFileDao) {
        this.documentDao = documentDao;
        this.docVerActions = docVerActions;
        this.dtoMapper = dtoMapper;
        this.catalogDao = catalogDao;
        this.docFileDao = docFileDao;
    }

    @Override
    public void deleteDocument(int documentId) {
        documentDao.deleteDocument(documentId);
    }

    @Override
    public DocumentDto loadDocument(int documentId) throws UnitNotFoundException {
        Document document = documentDao.loadDocument(documentId);
        if (document == null) {
            throw new UnitNotFoundException(documentId);
        } else {
            for (DocumentVersion documentVersion:document.getVersionsList()){
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
                        existinDoc.getDocumentType().getCurentType().equals(addingDocuemnt.getDocumentType().getCurentType())&&
                        unit.getId()!=addingDocuemnt.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    private DocumentDto createNewDocument(DocumentDto documentDto) throws UnitIsAlreadyExistException {
        Document document = dtoMapper.mapDocument(documentDto);
        if (checkIfDocumentExist(document)) {
            throw new UnitIsAlreadyExistException(catalogDao.loadCatalog(document.getCatalogId()), document);
        } else {
            return dtoMapper.mapDocument(documentDao.insertDocument(document));
        }
    }

    private List<Integer> getVerListForDelete(Document document) {
        Document dbDoc = documentDao.loadDocument(document.getId());
        List<Integer> idListForDelete = new ArrayList<>();
        for (DocumentVersion dbDocVersion : dbDoc.getVersionsList()) {
            if (!document.isVersionExist(dbDocVersion)) {
                idListForDelete.add(dbDocVersion.getId());
            }
        }
        return idListForDelete;
    }

    private Document insertVerList(Document document) throws VersionIsAlreadyExistException {
        Document dbDoc = documentDao.loadDocument(document.getId());
        List<DocumentVersion> listForInsert = new ArrayList<>();
        for (DocumentVersion docVersion : document.getVersionsList()) {
            if (!dbDoc.isVersionExist(docVersion)) {
                docVersion.setId(docVerActions.saveDocVersion(dtoMapper.mapDocVersion(docVersion)).getId());
            }
        }
        return document;
    }



    private void editDocuemnt(Document document) throws VersionIsAlreadyExistException {
        document = insertVerList(document);

        List<Integer> deleteList = getVerListForDelete(document);
        for (int id : deleteList) {
            docVerActions.deleteDocVersion(id);
        }
        documentDao.updateDocument(document);
    }

    @Override
    public DocumentDto saveDocument(DocumentDto documentDto) throws UnitIsAlreadyExistException, VersionIsAlreadyExistException {
        try {
            loadDocument(documentDto.getId());
            Document document = dtoMapper.mapDocument(documentDto);
            editDocuemnt(document);
            documentDto = loadDocument(documentDto.getId());
        } catch (UnitNotFoundException e) {
            documentDto = createNewDocument(documentDto);
        }
        return documentDto;
    }
}
