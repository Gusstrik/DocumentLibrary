package com.strelnikov.doclib.service.dtomapper.impl;

import com.strelnikov.doclib.dto.*;
import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.model.conception.UnitType;
import com.strelnikov.doclib.model.documnets.*;
import com.strelnikov.doclib.repository.DocTypeDao;
import com.strelnikov.doclib.repository.DocVersionDao;
import com.strelnikov.doclib.repository.DocumentDao;
import com.strelnikov.doclib.repository.jpa.DocFileJpa;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.model.catalogs.Catalog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DtoMapperImpl implements DtoMapper {

    @Autowired
    @Qualifier("DocVersionJpa")
    private DocVersionDao docVerDao;

    @Autowired
    @Qualifier("DocumentJpa")
    private DocumentDao docDao;

    @Autowired
    @Qualifier("DocTypeJpa")
    private DocTypeDao docTypeDao;


    @Override
    public UnitDto mapUnit(Unit unit) {
        if (unit.getId() == 1) {
            return new UnitDto(unit.getId(), unit.getName(), unit.getUnitType().toString(), 0);
        } else {
            return new UnitDto(unit.getId(), unit.getName(), unit.getUnitType().toString(), unit.getCatalogId());
        }
    }

    @Override
    public Unit mapUnit(UnitDto unitDto) {
        Unit unit;
        if (unitDto.getUnitType().equals(UnitType.CATALOG.toString())) {
            unit = new Catalog();
        } else {
            unit = new Document();
        }
        unit.setName(unitDto.getName());
        unit.setId(unitDto.getId());
        if (unitDto.getId() != 1) {
            unit.setCatalogId(unitDto.getParentId());
        } else {
            unit.setCatalogId(0);
        }
        return unit;
    }

    @Override
    public CatalogDto mapCatalog(Catalog catalog) {
        List<UnitDto> list = new ArrayList<>();
        for (Unit unit : catalog.getContentList()) {
            list.add(mapUnit(unit));
        }
        if (catalog.getId() == 1) {
            return new CatalogDto(catalog.getId(), catalog.getName(), 0, list);
        } else {
            return new CatalogDto(catalog.getId(), catalog.getName(), catalog.getCatalogId(), list);
        }
    }

    @Override
    public Catalog mapCatalog(CatalogDto catalogDto) {
        Catalog catalog = new Catalog();
        catalog.setId(catalogDto.getId());
        catalog.setName(catalogDto.getName());
        List<Unit> list = new ArrayList<>();
        for (UnitDto unitDto : catalogDto.getContentList()) {
            list.add(mapUnit(unitDto));
        }
        if (catalogDto.getParentId() != 0) {
            catalog.setCatalogId(catalogDto.getParentId());
        }
        catalog.setContentList(list);
        return catalog;
    }

    @Override
    public DocTypeDto mapDocType(DocumentType documentType) {
        return new DocTypeDto(documentType.getId(),documentType.getCurentType());
    }

    @Override
    public DocumentType mapDocType(DocTypeDto docTypeDto) {
        DocumentType documentType = new DocumentType();
        documentType.setId(docTypeDto.getId());
        documentType.setCurentType(docTypeDto.getDocType());
        return documentType;
    }

    @Override
    public DocFileDto mapDocFile(DocumentFile docFile) {
        return new DocFileDto(docFile.getId(), docFile.getFileName(), docFile.getFileName());
    }

    @Override
    public DocumentFile mapDocFile(DocFileDto docFileDto) {
        DocumentFile docFile = new DocumentFile();
        docFile.setId(docFileDto.getId());
        docFile.setFileName(docFileDto.getName());
        docFile.setFilePath(docFileDto.getPath());
        return docFile;
    }

    @Override
    public DocumentVersion mapDocVersion(DocVersionDto docVersionDto) {
        DocumentVersion docVersion = new DocumentVersion();
        docVersion.setId(docVersionDto.getId());
        if (docVersionDto.getDocumentId() != 0)
            docVersion.setParentDocument(docDao.loadDocument(docVersionDto.getDocumentId()));
        docVersion.setVersion(docVersionDto.getVersion());
        docVersion.setImportance(Importance.valueOf(docVersionDto.getImportance()));
        docVersion.setModerated(docVersionDto.isModerated());
        if (docVersionDto.getDescription() == null) {
            docVersion.setDescription("");
        } else {
            docVersion.setDescription(docVersionDto.getDescription());
        }
        List<DocumentFile> list = new ArrayList<>();
        for (DocFileDto fileDto : docVersionDto.getFileList()) {
            list.add(mapDocFile(fileDto));
        }
        docVersion.setFilesList(list);
        return docVersion;
    }

    @Override
    public DocVersionDto mapDocVersion(DocumentVersion documentVersion) {
        List<DocFileDto> list = new ArrayList<>();
        for (DocumentFile file : documentVersion.getFilesList()) {
            list.add(mapDocFile(file));
        }
        return new DocVersionDto(documentVersion.getId(), documentVersion.getParentDocument().getId(), documentVersion.getVersion(),
                documentVersion.getDescription(), documentVersion.getImportance().toString(), documentVersion.isModerated(), list);
    }

    @Override
    public Document mapDocument(DocumentDto documentDto) {
        Document document = new Document();
        document.setId(documentDto.getId());
        document.setDocumentType(docTypeDao.loadType(documentDto.getType()));
        document.setName(documentDto.getName());
        document.setCatalogId(documentDto.getCatalogId());
        document.setActualVersion(documentDto.getActualVersion());
        List<DocumentVersion> list = new ArrayList<>();
        for (DocVersionDto docVersionDto : documentDto.getVersionList()) {
            list.add(mapDocVersion(docVersionDto));
        }
        document.setVersionsList(list);
        return document;
    }

    @Override
    public DocumentDto mapDocument(Document document) {
        List<DocVersionDto> list = new ArrayList<>();
        for (DocumentVersion version : document.getVersionsList()) {
            list.add(mapDocVersion(version));
        }
        return new DocumentDto(document.getId(), document.getName(), document.getDocumentType().getId(),
                document.getActualVersion(), document.getCatalogId(), list);
    }
}

