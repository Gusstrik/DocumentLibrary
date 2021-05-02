package com.strelnikov.doclib.service.dtomapper.impl;

import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.dto.DocFileDto;
import com.strelnikov.doclib.dto.DocTypeDto;
import com.strelnikov.doclib.dto.DocumentDto;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentFile;
import com.strelnikov.doclib.model.documnets.DocumentType;
import com.strelnikov.doclib.model.documnets.DocumentVersion;
import com.strelnikov.doclib.service.DocumentActions;
import com.strelnikov.doclib.service.DocumentTypeActions;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.service.CatalogActions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DtoMapperImpl implements DtoMapper {

    private final CatalogActions catalogActions;
    private final DocumentActions documentActions;
    private final DocumentTypeActions documentTypeActions;

    public DtoMapperImpl(@Autowired CatalogActions catalogActions, @Autowired DocumentActions documentActions,
                         @Autowired DocumentTypeActions documentTypeActions){
        this.catalogActions= catalogActions;
        this.documentActions = documentActions;
        this.documentTypeActions=documentTypeActions;
    }


    @Override
    public CatalogDto mapCatalog(String catalogName) {
        Catalog catalog = catalogActions.loadCatalog(catalogName);
        return new CatalogDto(catalog);
    }

    @Override
    public void mapCatalog(CatalogDto catalog) {
        if (catalog.getParent() == null || catalog.getParent().isEmpty()) {
            catalogActions.createNewCatalog(catalog.getName());
        } else {
            catalogActions.createNewCatalog(catalog.getName(), catalog.getParent());
        }
    }

    @Override
    public DocTypeDto mapDocType() {
        documentTypeActions.refreshListDocumentType();
        return new DocTypeDto(DocumentType.documentTypeList);
    }

    @Override
    public DocumentDto mapDocument(DocumentDto documentDto) {
        Document document = documentActions.loadDocument(documentDto.getName(), new DocumentType(documentDto.getType()));
        document.setActualVersion(documentDto.getVersion());
        documentActions.refreshDocumentsFileList(document);
        DocumentVersion documentVersion = document.getDocumentVersion(documentDto.getVersion());
        List<DocFileDto> fileDtoList = new ArrayList<>();
        for (DocumentFile docFile : documentVersion.getFilesList()) {
            fileDtoList.add(new DocFileDto(docFile.getFileName(), docFile.getFilePath()));
        }
        return new DocumentDto(document.getName(), documentDto.getVersion(), documentDto.getType(), fileDtoList, documentVersion.isModerated());

    }

    @Override
    public void mapNewDocument(DocumentDto documentDto) {
        Catalog catalog = new Catalog(documentDto.getCatalogName());
        documentActions.createNewDocument(documentDto.getName(),new DocumentType(documentDto.getType()),catalog);
    }

    @Override
    public void mapNewDocVersion(DocumentDto documentDto) {
        Catalog catalog = new Catalog(documentDto.getCatalogName());
        Document document = documentActions.loadDocument(documentDto.getName(), new DocumentType(documentDto.getType()));
        document.setActualVersion(document.getActualVersion()+1);
        DocumentVersion documentVersion = new DocumentVersion();
        documentVersion.setModerated(false);
        for (DocFileDto fileDto:documentDto.getFileList()){
            documentVersion.getFilesList().add(new DocumentFile(fileDto.getName(),fileDto.getPath()));
        }
        document.getVersionsList().add(documentVersion);
        documentActions.createNewDocumentVersion(document,catalog);
    }


}
